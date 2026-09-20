package com.generated.waterLeak.types;

/** 派工请求体：workDate 为空时默认当天。 */
public record RepairOrderPayload(Long leakReportId, Long crewId, String workDate, String priority) {}
