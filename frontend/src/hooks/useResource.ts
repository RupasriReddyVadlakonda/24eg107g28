import { useCallback, useEffect, useState, type Dispatch, type SetStateAction } from 'react';

export interface ResourceState<T> {
  data: T | null;
  loading: boolean;
  error: unknown;
  reload: () => void;
  setData: Dispatch<SetStateAction<T | null>>;
}

export function useResource<T>(load: () => Promise<T>): ResourceState<T> {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);
  const [revision, setRevision] = useState(0);

  const reload = useCallback(() => {
    setLoading(true);
    setError(null);
    setRevision((current) => current + 1);
  }, []);

  useEffect(() => {
    let active = true;
    load().then((result) => {
      if (active) setData(result);
    }).catch((reason: unknown) => {
      if (active) setError(reason);
    }).finally(() => {
      if (active) setLoading(false);
    });
    return () => { active = false; };
  }, [load, revision]);

  return { data, loading, error, reload, setData };
}
