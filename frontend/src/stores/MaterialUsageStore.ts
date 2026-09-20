import { create } from "zustand";
import { listMaterialStock, listMaterialUsage } from "../api/MaterialUsage";
import type { MaterialStock } from "../types/MaterialStock";
import type { MaterialUsage } from "../types/MaterialUsage";

type State = {
  rows: MaterialUsage[];
  stock: MaterialStock[];
  loading: boolean;
  load: (repairOrderId?: number) => Promise<void>;
  loadStock: () => Promise<void>;
};

export const useMaterialUsageStore = create<State>((set) => ({
  rows: [],
  stock: [],
  loading: false,
  async load(repairOrderId) {
    set({ loading: true });
    set({ rows: await listMaterialUsage(repairOrderId), loading: false });
  },
  async loadStock() {
    set({ stock: await listMaterialStock() });
  }
}));
