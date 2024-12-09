package com.pluralsight;

import com.pluralsight.avro.AudioCommand;

public class CommandBuilder {


    public static void main(String[] args) {
        AudioCommand command = AudioCommand.newBuilder()
                .setCommand("Hello Pluralsight!")
                .build();

        System.out.println(command.toString());
    }
}
