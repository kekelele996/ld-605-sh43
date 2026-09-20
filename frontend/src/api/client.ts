import { ERROR_MESSAGES } from "../constants/errorMessages";

/** 后端业务错误：{code, message}，HTTP 状态非 2xx。 */
export class ApiError extends Error {
  code: string;
  status: number;

  constructor(code: string, message: string, status: number) {
    super(message);
    this.code = code;
    this.status = status;
  }
}

export type ApiFailure = { code: string; message: string };

export function toFailure(err: unknown): ApiFailure {
  if (err instanceof ApiError) return { code: err.code, message: err.message };
  return { code: "INTERNAL_ERROR", message: ERROR_MESSAGES.INTERNAL_ERROR };
}

async function parseError(res: Response): Promise<ApiError> {
  try {
    const body = (await res.json()) as { code?: string; message?: string };
    return new ApiError(body.code ?? "INTERNAL_ERROR", body.message ?? res.statusText, res.status);
  } catch {
    return new ApiError("INTERNAL_ERROR", `${res.status} ${res.statusText}`, res.status);
  }
}

/** 统一请求封装：业务错误抛 ApiError；网络错误原样抛出（调用方决定回退本地 mock）。 */
export async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const res = await fetch(path, {
    headers: { "Content-Type": "application/json" },
    ...options
  });
  if (!res.ok) throw await parseError(res);
  if (res.status === 204) return undefined as T;
  return (await res.json()) as T;
}

export const get = <T>(path: string) => request<T>(path);
export const post = <T>(path: string, body: unknown) =>
  request<T>(path, { method: "POST", body: JSON.stringify(body) });
