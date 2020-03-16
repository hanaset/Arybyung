package com.how.arybyungcommon.client.joonggonara.model;

import lombok.Data;

@Data
public class JoonggonaraMessage {

    private String status;

    private JoonggonaraError error;

    private JoonggonaraResult result;
}
