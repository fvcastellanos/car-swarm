package edu.umg.car;

import edu.umg.car.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@ComponentScan
@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private CarService carService;

    @Override
    public void run(String... args) {
        try {

            String command = "";

            Scanner scanner = new Scanner(System.in);

            while (!command.equals("q")) {
                System.out.print("Enter your command: ");
                command = scanner.nextLine();

                switch (command) {
                    case "f": carService.forward(); break;
                    case "b": carService.back(); break;
                    case "l": carService.left(); break;
                    case "r": carService.right(); break;
                    case "s": carService.stop(); break;
                    case "p": carService.ping(); break;
                }
            }

            carService.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

}
