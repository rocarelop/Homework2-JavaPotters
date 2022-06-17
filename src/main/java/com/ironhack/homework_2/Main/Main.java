package com.ironhack.homework_2.Main;

import com.ironhack.homework_2.CRM.CRM;
import com.ironhack.homework_2.CRM.Webinar;
import com.ironhack.homework_2.Classes.Account;
import com.ironhack.homework_2.Classes.Contact;
import com.ironhack.homework_2.Classes.Lead;
import com.ironhack.homework_2.Classes.Opportunity;
import com.ironhack.homework_2.Enumerations.IndustryEnum;
import com.ironhack.homework_2.Enumerations.ProductEnum;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Webinar webinar = new Webinar();

        //Create a Scanner to collect user input
        Scanner myScanner2 = new Scanner(System.in);

        System.out.println("************* Starting CRM *************");
        System.out.println("What is your name?");
        String salesAssistName = myScanner2.nextLine();
        System.out.println("Welcome to the CRM system, " + salesAssistName + ".\n" + options);

        CRM crm = new CRM();

        while (true) {
            Scanner myScanner = new Scanner(System.in);
            System.out.println("What do you want to do, " + salesAssistName + "?");
            String userInput = myScanner.nextLine();
            if(isAKeyWord(userInput)){
                userInput = userInput.toLowerCase();
                int id;
                try{
                    String[] splited = userInput.split(" ");
                    String keyword = splited[0].toLowerCase();
                    switch (keyword){
                        case "convert":
                            id = Integer.parseInt(splited[1]);
                            Lead lead = crm.findLead(id);
                            Contact contact = crm.createContact(lead);

                            int productQuantity = validInput("How many trucks does the lead want?");
                            int productTypeInt = validInput("Which of our products? \n" +
                                    "1. HYBRID,\n" + "2. FLATBED,\n" + "3. BOX");

                            ProductEnum productEnum = null;
                            switch (productTypeInt){
                                case 1:
                                    productEnum = ProductEnum.HYBRID;
                                    break;
                                case 2:
                                    productEnum = ProductEnum.FLATBED;
                                    break;
                                case 3:
                                    productEnum = ProductEnum.BOX;
                                    break;
                            }

                            Opportunity opportunity = crm.createOpportunity(contact, productEnum, productQuantity);

                            int industryTypeInt = validInput("Which industry are the products for? \n" +
                                    "1. PRODUCE\n" + "2. ECOMMERCE\n" + "3. MANUFACTURING\n" + "4. MEDICAL\n" +
                                    "5. OTHER");

                            IndustryEnum industryEnum = null;
                            switch (industryTypeInt){
                                case 1:
                                    industryEnum = IndustryEnum.PRODUCE;
                                    break;
                                case 2:
                                    industryEnum = IndustryEnum.ECOMMERCE;
                                    break;
                                case 3:
                                    industryEnum = IndustryEnum.MANUFACTURING;
                                    break;
                                case 4:
                                    industryEnum = IndustryEnum.MEDICAL;
                                    break;
                                case 5:
                                    industryEnum = IndustryEnum.OTHER;
                                    break;
                            }

                            int numEmployees = validInput("How many employees does the contact's company have?");
                            List<String> companyData = getInputData(
                                    "\nIntroduce the city where the contact's company is located.",
                                    "\nIntroduce the country where the contact's company is located.");

                            Account account = crm.createAccount(industryEnum, numEmployees,
                                    companyData.get(0), companyData.get(1), contact, opportunity);
                            crm.deleteLead(lead);
                            System.out.println("Convert done");
                            break;
                        case "lookup":
                            id = Integer.parseInt(splited[1]);
                            Opportunity opportunity1 = crm.lookUpOpportunity(id);
                            System.out.println(opportunity1);
                            break;
                        case "close-lost":
                            id = Integer.parseInt(splited[1]);
                            Opportunity opportunity2 = crm.closeOpportunity("Lost", id);
                            System.out.println("The opportunity with id " + id + "was closed-lost");
                            // System.out.println(opportunity2.getStatus());
                            break;
                        case "close-won":
                            id = Integer.parseInt(splited[1]);
                            Opportunity opportunity3 = crm.closeOpportunity("Won", id);
                            System.out.println("The opportunity with id " + id + "was closed-won");
                            // System.out.println(opportunity3.getStatus());
                            break;
                        case "show":
                            String objectType = splited[1];
                            if (objectType.equals("leads")) {
                                System.out.println("List of leads: ");
                                crm.showListLead();
                            } else if (objectType.equals("opportunities")) {
                                System.out.println("List of opportunities:  ");
                                crm.showListOpportunity();
                            } else{
                                System.out.println("Invalid option, please try again.");
                                break;
                            }
                            break;
                        case "signup":
                            lead = webinar.signingUp();
                            crm.addLead(lead);
                            break;
                        case "exit":
                            System.exit(0);
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("Please insert ID");
                }

            }
            else {
                System.out.println("Invalid option, please try again.");
                System.out.println(options);
            }
        }
    }

    private static final String options = "You can type: \n" +
            "\033[3m- Signup:\033[0m to create a lead.\n" +
            "\033[3m- Show leads:\033[0m  to see the leads list.\n" +
            "\033[3m- Convert <leadId>:\033[0m to convert a lead into an opportunity.\n" +
            "\033[3m- Show opportunities:\033[0m to see the opportunities list.\n" +
            "\033[3m- Lookup <opportunityId>:\033[0m to find an opportunity.\n" +
            "\033[3m- Close-won <opportunityId>:\033[0m to close an opportunity that was won.\n" +
            "\033[3m- Close-lost <opportunityId>:\033[0m to close an opportunity that was lost.\n"+
            "\033[3m- Exit:\033[0m to finish the program.\n";
    private static final String[] keyWords = {"convert", "lookup", "close-lost", "close-won", "show", "signup", "exit"};

    public static boolean isAKeyWord(String str){
        String[] splited = str.split(" ");
        String word = splited[0].toLowerCase();
        for(String keyWord: keyWords){
            if(word.equals(keyWord)){
                return true;
            }
        }
        return false;
    }

    private static List<String> getInputData(String... questions) {
        Scanner scanner = new Scanner(System.in);
        List<String> inputData = new ArrayList<>();
        for (String question : questions) {
            System.out.println(question);
            inputData.add(scanner.next());
        }
        return inputData;
    }

    private static int validInput(String questions){
        Scanner myScanner =  new Scanner(System.in);
        int input = 0;

        while (input <= 0){
            try {System.out.println(questions);
                input = myScanner.nextInt();
            } catch (InputMismatchException ex) {
                ex.getMessage();
                System.out.println("INVALID OPTION! Please insert an int" );
                myScanner =  new Scanner(System.in);
            }
        }
        return input;
    }

}
