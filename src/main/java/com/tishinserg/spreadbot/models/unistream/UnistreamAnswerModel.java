package com.tishinserg.spreadbot.models.unistream;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UnistreamAnswerModel {
    private String message;
    @JsonProperty("fees")
    List<UnistreamAnswerDetailedJson> fees;
}
