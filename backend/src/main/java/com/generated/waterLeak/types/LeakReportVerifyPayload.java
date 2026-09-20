package com.generated.waterLeak.types;

import jakarta.validation.constraints.NotBlank;

/** 漏损核实请求体：result 取 VERIFIED / REJECTED。 */
public record LeakReportVerifyPayload(@NotBlank String result) {}
