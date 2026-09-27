package com.wyu4.snowberryjam.cli;

public class Snowb {
  public static void main(String[] args) {
    if (args.length <= 0) {
      System.out.println("\n\n\n=====================================================");
      System.out.println("""

            █▀▀▀▀▀▀▀█                       █
            ▀▄▀███▀▄▀     ▄▀▀ █▀▄ ▄▀▄ █ ▄ █ █▀▄ ▄█▄ ▄▀▀ ▄▀▀ █ █
           ▄▀▄█████▄▀▄    ▄▄▀ █ █ ▀▄▀ ▀▄▀▄▀ █▄▀ ▀▄▄ █   █   ▀▄█
          █ ██▀███▀██ █                                     ▄▄▀
          █ ██▄   ▄██ █    ▀                  █ ▀
          █ ██     ██ █    █ ▀█▄ █▀█▀▄    ▄▀▀ █ █
          █ ███▄█▄███ █    █ ▀▄█ █ █ █    ▀▄▄ █ █
           ▀▄▀▀▀▀▀▀▀▄▀    ▄▀
             ▀▀▀▀▀▀▀

          """);
      System.out.println("Welcome to the Snowberry Jam terminal client!\n\nIf this is your first time using this, please visit https://snowberry-jam.wyu.app/ for the language guide.\nThis command can be used to run source files straight from your terminal.\nPlease use the following command to do so:\n\n> snowb [PATH_TO_SOURCE_FILE]\n\n");
    }
  }
}
