package com.generated.waterLeak.types;

/** 漏损上报请求体。 */
public record LeakReportPayload(Long pointId, String leakLevel, String reporterType, String description, String photoUrl) {}
