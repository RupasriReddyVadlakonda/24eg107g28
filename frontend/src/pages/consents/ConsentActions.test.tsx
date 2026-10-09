import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ConsentRequestsPage } from './ConsentRequestsPage';
import { ConsentsPage } from './ConsentsPage';

const service = vi.hoisted(() => ({ list: vi.fn(), grant: vi.fn(), deny: vi.fn(), revoke: vi.fn() }));
vi.mock('../../services/consentService', () => ({ consentService: service }));
vi.mock('../../hooks/useToast', () => ({ useToast: () => ({ showToast: vi.fn() }) }));

const item = { id: 7, userId: 3, applicationId: 9, applicationName: 'ShoppingApp', dataType: 'EMAIL' as const, purpose: 'Order confirmation', allowedOperation: 'READ' as const, status: 'PENDING' as const, startTime: null, expirationTime: null, requestedDurationDays: 7, createdAt: '2026-09-30T10:00:00', revokedAt: null };

function confirmButtons() { return screen.getAllByRole('button', { name: 'Grant access' }); }

describe('consent actions', () => {
  beforeEach(() => { vi.clearAllMocks(); });

  it('confirms and sends a grant request to the consent API', async () => {
    const user = userEvent.setup();
    service.list.mockResolvedValue([item]); service.grant.mockResolvedValue({ ...item, status: 'GRANTED' });
    render(<MemoryRouter><ConsentRequestsPage /></MemoryRouter>);
    await screen.findAllByText('ShoppingApp');
    await user.click(confirmButtons()[0]);
    const dialogs = await screen.findAllByRole('dialog');
    const dialog = dialogs.find((node) => node.getAttribute('open') !== null);
    expect(dialog).toBeTruthy();
    await user.click(withinDialogButton(dialog!, 'Grant access'));
    await waitFor(() => expect(service.grant).toHaveBeenCalledWith(7));
  });

  it('confirms and sends a revoke request for granted consent', async () => {
    const user = userEvent.setup();
    service.list.mockResolvedValue([{ ...item, status: 'GRANTED', startTime: '2026-09-30T10:00:00', expirationTime: '2027-09-30T10:00:00' }]);
    service.revoke.mockResolvedValue({ ...item, status: 'REVOKED' });
    render(<MemoryRouter><ConsentsPage /></MemoryRouter>);
    await user.click(await screen.findByRole('button', { name: 'Revoke' }));
    const dialog = await screen.findByRole('dialog');
    await user.click(withinDialogButton(dialog, 'Revoke consent'));
    await waitFor(() => expect(service.revoke).toHaveBeenCalledWith(7));
  });
});

function withinDialogButton(dialog: HTMLElement, label: string): HTMLElement {
  const button = [...dialog.querySelectorAll('button')].find((item) => item.textContent?.trim() === label);
  if (!button) throw new Error(`Missing ${label} dialog action`);
  return button;
}
