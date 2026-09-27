package com.wyu4.snowberryjam.cli;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wyu4.snowberryjam.ResourceUtils;
import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;

/**
 * The entry class for the terminal client of Snowberry Jam.
 * 
 * @see LocalStorage
 * @see Compiler
 */
public class Snowb {
  /**
   * The entry point of the terminal client
   * 
   * @param args Command arguments in the following order: [SOURCE_FILE] [VERBOSE
   *             (-v)]
   * @see Compiler#VERBOSE
   */
  public static void main(String[] args) {
    Compiler.VERBOSE = false;

    if (args.length <= 0) {
      System.out.println("\n\n\n=====================================================\n");
      System.out.println("""
            █▀▀▀▀▀▀▀█                       █
            ▀▄▀███▀▄▀     ▄▀ ▀ █▀▄ ▄▀▄ █ ▄ █ █▀▄ ▄█▄ ▄▀▀ ▄▀▀ █ █
           ▄▀▄█████▄▀▄    ▄▄▀ █ █ ▀▄▀ ▀▄▀▄▀ █▄▀ ▀▄▄ █   █   ▀▄█
          █ ██▀███▀██ █                                     ▄▄▀
          █ ██▄   ▄██ █    ▀                  █ ▀
          █ ██     ██ █    █ ▀█▄ █▀█▀▄    ▄▀▀ █ █
          █ ███▄█▄███ █    █ ▀▄█ █ █ █    ▀▄▄ █ █
           ▀▄▀▀▀▀▀▀▀▄▀    ▄▀
             ▀▀▀▀▀▀▀      
          """);
      System.out.println(
          "Welcome to the Snowberry Jam terminal client!\n\nIf this is your first time using this, please visit https://snowberry-jam.wyu.app/ for the language guide.\nThis command can be used to run source files straight from your terminal.\nPlease use the following command to do so:\n\n> snowb [PATH_TO_SOURCE_FILE]\n\n");
      System.exit(0);
    }

    final AtomicBoolean running = new AtomicBoolean(false);
    Thread inputDaemon = new Thread(() -> {
      try (Scanner scanner = new Scanner(System.in)) {
        while (System.console() != null && running.get() && scanner.hasNextLine()) {
          final String input = scanner.nextLine();
          if (input.equalsIgnoreCase("exit")) {
            System.out.println("User force-terminated.");
            break;
          }
          LocalStorage.sendInput(input);
        }
      }
    });
    inputDaemon.setDaemon(true);

    final String path = args[0];
    final File file = new File(path);
    if (!file.exists()) {
      System.err.println("File [%s] does not exist.".formatted(path));
      System.exit(1);
    }

    if (file.isDirectory()) {
      System.err.println("[%s] is a directory.".formatted(path));
      System.exit(1);
    }

    if (!file.canRead()) {
      System.err.println("Missing read permissions for file [%s]".formatted(path));
      System.exit(1);
    }

    // Compiling & running
    if (Compiler.VERBOSE) {
      System.out.println("Reading [%s]...".formatted(path));
    }
    ;

    final String source = ResourceUtils.readFile(file);
    try {
      Compiler.compile(source);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      System.exit(1);
    }

    running.set(true);
    inputDaemon.start();
    LocalStorage.runStack();
    running.set(false);
  }
}
