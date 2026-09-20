import { create } from "zustand";
import { listPipelineSegment } from "../api/PipelineSegment";
import type { PipelineSegment } from "../types/PipelineSegment";

type State = { rows: PipelineSegment[]; loading: boolean; load: () => Promise<void> };

export const usePipelineSegmentStore = create<State>((set) => ({
  rows: [],
  loading: false,
  async load() {
    set({ loading: true });
    set({ rows: await listPipelineSegment(), loading: false });
  }
}));
