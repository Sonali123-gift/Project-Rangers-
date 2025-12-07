package org.global.academy;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class Server {

    // Simple flashcard class for the web API
    static class Flashcard {

        String front;
        String back;

        Flashcard(String front, String back) {
            this.front = front;
            this.back = back;
        }
    }

    // Response classes for login
    static class LoginResponse {

        String username;
        String token;

        LoginResponse(String username, String token) {
            this.username = username;
            this.token = token;
        }
    }

    static class ErrorResponse {

        String error;

        ErrorResponse(String error) {
            this.error = error;
        }
    }

    public static void main(String[] args) {

        // Port and static files (HTML)
        port(8080);                      // http://localhost:4567
        staticFiles.location("/public"); // src/main/resources/public

        Gson gson = new Gson();

        // ---------------- LOGIN ----------------
        post("/login", (req, res) -> {
            res.type("application/json");

            System.out.println("Received /login request with body: " + req.body());

            JsonObject body = gson.fromJson(req.body(), JsonObject.class);
            String username = body.get("username").getAsString();
            String password = body.get("password").getAsString();

            if ("alice".equals(username) && "secret".equals(password)) {
                res.status(200);
                return gson.toJson(new LoginResponse(username, "token123"));
            } else {
                res.status(401);
                return gson.toJson(new ErrorResponse("Invalid username or password"));
            }
        });

        // ------------- THAI FLASHCARDS -------------
        // This is the route your welcome.html calls: /showrandcard
        List<Flashcard> cards = Arrays.asList(
                new Flashcard("ง (งอ งู)", "ngo ngu = snake"),
                new Flashcard("จ (จอ จาน)", "cho chan = plate"),
                new Flashcard("ฉ (ฉอ ฉิ่ง)", "cho ching = cymbals"),
                new Flashcard("ช (ชอ ช้าง)", "cho chang = elephant"),
                new Flashcard("ซ (ซอ โซ่)", "so so = chain"),
                new Flashcard("ฌ (ฌอ เฌอ)", "cho choe = tree"),
                new Flashcard("ญ (ญอ หญิง)", "yo ying = woman"),
                new Flashcard("ฎ (ฎอ ชฎา)", "do cha-da = crown / head-dress"),
                new Flashcard("ฏ (ฏอ ปฏัก)", "to pa-tak = spear"),
                new Flashcard("ฐ (ฐอ ฐาน)", "tho than = base"),
                new Flashcard("ฑ (ฑอ มณโฑ)", "tho mon-tho = Mandodari (character)"),
                new Flashcard("ฒ (ฒอ ผู้เฒ่า)", "tho phu-thao = old person"),
                new Flashcard("ก (กอ ไก่)", "ko kai - mid class")
        );

       get("/randflashcard", (req, res) -> {
            res.type("application/json");
            Random r = new Random();
            Flashcard card = cards.get(r.nextInt(cards.size()));
            return gson.toJson(card);
        });

        System.out.println("✅ Server running on http://localhost:8080");
    }
}
