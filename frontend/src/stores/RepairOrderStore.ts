import { create } from "zustand";
import { listRepairOrder } from "../api/RepairOrder";
import type { RepairOrder } from "../types/RepairOrder";

type State = { rows: RepairOrder[]; loading: boolean; load: () => Promise<void> };

export const useRepairOrderStore = create<State>((set) => ({
  rows: [],
  loading: false,
  async load() {
    set({ loading: true });
    set({ rows: await listRepairOrder(), loading: false });
  }
}));
