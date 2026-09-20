import { create } from "zustand";
import { listLeakReport } from "../api/LeakReport";
import type { LeakReport } from "../types/LeakReport";

type State = { rows: LeakReport[]; loading: boolean; load: () => Promise<void> };

export const useLeakReportStore = create<State>((set) => ({
  rows: [],
  loading: false,
  async load() {
    set({ loading: true });
    set({ rows: await listLeakReport(), loading: false });
  }
}));
