import { create } from "zustand";
import { listInspectionPoint } from "../api/InspectionPoint";
import type { InspectionPoint } from "../types/InspectionPoint";

type State = { rows: InspectionPoint[]; loading: boolean; load: () => Promise<void> };

export const useInspectionPointStore = create<State>((set) => ({
  rows: [],
  loading: false,
  async load() {
    set({ loading: true });
    set({ rows: await listInspectionPoint(), loading: false });
  }
}));
