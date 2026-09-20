import { ERROR_MESSAGES } from "../constants/errorMessages";

/** 后端统一错误结构 {code, message} 的前端表示。 */
export class ApiError extends Error {
  code: string;
  constructor(code: string, message: string) {
    super(message);
    this.code = code;
  }
}

async function parseError(res: Response): Promise<ApiError> {
  try {
    const body = await res.json();
    if (body && typeof body.message === "string") {
      return new ApiError(String(body.code ?? "UNKNOWN"), body.message);
    }
  } catch {
    // fall through to generic error
  }
  return new ApiError("HTTP_" + res.status, ERROR_MESSAGES.UNKNOWN);
}

/** 统一请求封装：/api 前缀由 nginx / vite 代理到后端。 */
export async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const res = await fetch(path, {
    headers: { "Content-Type": "application/json", ...(options.headers ?? {}) },
    ...options
  });
  if (!res.ok) throw await parseError(res);
  return (await res.json()) as T;
}

export async function post<T>(path: string, body: unknown): Promise<T> {
  return request<T>(path, { method: "POST", body: JSON.stringify(body) });
}
