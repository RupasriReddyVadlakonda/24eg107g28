import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { VaultPage } from './VaultPage';

const list = vi.hoisted(() => vi.fn());
vi.mock('../../services/vaultService', () => ({ vaultService: { list, get: vi.fn(), create: vi.fn(), update: vi.fn(), remove: vi.fn() } }));
vi.mock('../../hooks/useToast', () => ({ useToast: () => ({ showToast: vi.fn() }) }));

const record = { id: 1, dataType: 'EMAIL' as const, value: 'private@example.test', description: 'Primary email', createdAt: '2026-01-01T00:00:00', updatedAt: '2026-01-02T00:00:00' };

describe('vault rendering', () => {
  beforeEach(() => { vi.clearAllMocks(); list.mockResolvedValue([record]); });

  it('masks an API-returned sensitive value until explicitly revealed', async () => {
    const user = userEvent.setup();
    render(<VaultPage />);
    const revealButtons = await screen.findAllByRole('button', { name: 'Reveal email value' });
    expect(screen.queryByText('private@example.test')).not.toBeInTheDocument();
    expect(screen.getAllByText(/p•+@example\.test/).length).toBeGreaterThan(0);
    await user.click(revealButtons[0]);
    expect(screen.getByText('private@example.test')).toBeInTheDocument();
    expect(await screen.findAllByRole('button', { name: 'Hide email value' })).toHaveLength(1);
  });
});
