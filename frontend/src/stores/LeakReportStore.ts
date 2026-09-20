import { create } from "zustand";
import { createLeakReport, listLeakReport, verifyLeakReport, type LeakReportCreatePayload } from "../api/LeakReport";
import { ApiError } from "../api/http";
import type { LeakReportView } from "../types/LeakReportView";

type State = {
  rows: LeakReportView[];
  loading: boolean;
  load: () => Promise<void>;
  /** 上报：成功返回 null，失败返回错误消息。 */
  create: (payload: LeakReportCreatePayload) => Promise<string | null>;
  /** 核实：成功返回 null，失败返回错误消息。 */
  verify: (id: number, result: "VERIFIED" | "REJECTED") => Promise<string | null>;
};

function toMessage(e: unknown): string {
  if (e instanceof ApiError) return e.message;
  if (e instanceof Error) return e.message;
  return "操作失败，请稍后重试";
}

export const useLeakReportStore = create<State>((set, get) => ({
  rows: [],
  loading: false,
  async load() {
    set({ loading: true });
    set({ rows: await listLeakReport(), loading: false });
  },
  async create(payload) {
    try {
      await createLeakReport(payload);
      await get().load();
      return null;
    } catch (e) {
      return toMessage(e);
    }
  },
  async verify(id, result) {
    try {
      await verifyLeakReport(id, result);
      await get().load();
      return null;
    } catch (e) {
      return toMessage(e);
    }
  }
}));
