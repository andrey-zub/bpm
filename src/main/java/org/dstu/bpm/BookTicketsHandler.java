package org.dstu.bpm;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Scanner;

@Component
@ExternalTaskSubscription("book-tickets")
public class BookTicketsHandler implements ExternalTaskHandler {
    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        Map<String, Object> vars = externalTask.getAllVariables();
        Object[] showList = (Object[]) vars.get("showList");
        Scanner scanner = new Scanner(System.in);

        System.out.println("---------------------------------");
        System.out.println("Show List:");
        System.out.println("---------------------------------");

        for (Object show : showList) {
            if (show instanceof Map) {
                Map<String, Object> showData = (Map<String, Object>) show;
                String title = (String) showData.get("title");
                Long startAt = (Long) showData.get("startAt");
                Integer duration = (Integer) showData.get("duration");
                String hall = (String) showData.get("hall");
                String ident = (String) showData.get("ident");


                System.out.println("Show: " + title);
                System.out.println("Start Time: " + startAt);
                System.out.println("Duration: " + duration);
                System.out.println("Hall: " + hall);
                System.out.println("Identificator: " + ident);
                System.out.println("---------------------------------");
            }
        }

        System.out.print("Enter the title of the show you want to book: ");
        String selectedShow = scanner.nextLine().trim();
        String getURL = "http://localhost:8091/show/title/" + selectedShow;

        RestTemplate rt = new RestTemplate();

        Map<String, Object> show = rt.getForObject(getURL, Map.class);
        if (show != null && !show.isEmpty()) {
            System.out.println("show found. Here is your ticket");
            System.out.println("Title: " + show.get("title"));
            System.out.println("Start Time: " + show.get("startAt"));
            System.out.println("Duration: " + show.get("duration"));
            System.out.println("Hall: " + show.get("hall"));
            System.out.println("Identificator: " + show.get("ident"));
            System.out.println("---------------------------------");
        } else {
            System.out.println("Movie not found.");
        }

        externalTaskService.complete(externalTask);
    }
}
