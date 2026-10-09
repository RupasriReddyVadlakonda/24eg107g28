import { describe, expect, it } from 'vitest';
import { ApiError, httpErrorMessage, toUserMessage } from './api';

describe('API error presentation', () => {
  it('uses the required permission message for 403', () => {
    expect(httpErrorMessage(403)).toBe('You do not have permission to perform this action.');
    expect(new ApiError('denied', 403).status).toBe(403);
  });

  it('preserves the correct HTTP status and message', () => {
    const tooManyRequests = new ApiError(httpErrorMessage(429), 429);
    expect(toUserMessage(tooManyRequests)).toBe('Too many requests. Please try again later.');
    expect(tooManyRequests.status).toBe(429);
  });
});
