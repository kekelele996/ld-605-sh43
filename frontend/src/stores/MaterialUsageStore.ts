import { create } from "zustand";
import { listMaterialUsage } from "../api/MaterialUsage";
import type { MaterialUsage } from "../types/MaterialUsage";

type State = { rows: MaterialUsage[]; loading: boolean; load: () => Promise<void> };

export const useMaterialUsageStore = create<State>((set) => ({
  rows: [],
  loading: false,
  async load() {
    set({ loading: true });
    set({ rows: await listMaterialUsage(), loading: false });
  }
}));
