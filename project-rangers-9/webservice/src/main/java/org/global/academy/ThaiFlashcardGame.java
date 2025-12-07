package org.global.academy;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

class Flashcard {
    protected String front;
    protected String back;

    public Flashcard(String front, String back) {
        this.front = front;
        this.back = back;
    }

    public String getFront() { return front; }
    public String getBack() { return back; }
}

class ThaiConsonantFlashcard extends Flashcard {
    private String symbol;
    private String thaiReading;
    private String romanizedPronunciation;
    private String toneClass;

    public ThaiConsonantFlashcard(String symbol, String thaiReading,
                                  String romanizedPronunciation, String toneClass) {
        super(symbol + " " + thaiReading, romanizedPronunciation + " (" + toneClass + ")");
        this.symbol = symbol;
        this.thaiReading = thaiReading;
        this.romanizedPronunciation = romanizedPronunciation;
        this.toneClass = toneClass;
    }

    public String getCorrectAnswer() {
        return romanizedPronunciation;
    }
}

public class ThaiFlashcardGame extends JFrame {
    private ArrayList<ThaiConsonantFlashcard> cards = new ArrayList<>();
    private ThaiConsonantFlashcard currentCard;
    private JLabel questionLabel;
    private JButton[] answerButtons = new JButton[3];
    private int score = 0;
    private int index = 0;

    public ThaiFlashcardGame() {
        setTitle("Thai Consonant Flashcard Game 🎮");
        setSize(450, 350);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Sample dataset — You can add more
        cards.add(new ThaiConsonantFlashcard("ก", "กอ ไก่", "ko kai", "mid"));
        cards.add(new ThaiConsonantFlashcard("ข", "ขอ ไข่", "kho khai", "high"));
        cards.add(new ThaiConsonantFlashcard("ค", "คอ ควาย", "kho khwai", "low"));
        cards.add(new ThaiConsonantFlashcard("ง", "งอ งู", "ngo ngu", "low"));
        cards.add(new ThaiConsonantFlashcard("จ", "จอ จาน", "cho chan", "mid"));

        Collections.shuffle(cards);

        // UI
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(questionLabel, BorderLayout.NORTH);

        JPanel answersPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        for (int i = 0; i < 3; i++) {
            answerButtons[i] = new JButton();
            answerButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));
            answerButtons[i].setBackground(Color.LIGHT_GRAY);
            answerButtons[i].addActionListener(new AnswerListener());
            answersPanel.add(answerButtons[i]);
        }
        add(answersPanel, BorderLayout.CENTER);

        loadNextCard();
    }

    private void loadNextCard() {
        if (index >= cards.size()) {
            endGame();
            return;
        }

        currentCard = cards.get(index);
        questionLabel.setText("🃏 " + currentCard.getFront());

        ArrayList<String> options = new ArrayList<>();
        options.add(currentCard.getCorrectAnswer());

        // Add random wrong answers
        Random r = new Random();
        while (options.size() < 3) {
            String wrong = cards.get(r.nextInt(cards.size())).getCorrectAnswer();
            if (!options.contains(wrong)) options.add(wrong);
        }

        Collections.shuffle(options);

        for (int i = 0; i < 3; i++)
            answerButtons[i].setText(options.get(i));
    }

    private class AnswerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            String answer = clicked.getText();

            if (answer.equals(currentCard.getCorrectAnswer())) {
                score++;
                clicked.setBackground(Color.GREEN);
            } else {
                clicked.setBackground(Color.RED);
            }

            Timer t = new Timer(650, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    for (JButton b : answerButtons)
                        b.setBackground(Color.LIGHT_GRAY);
                    index++;
                    loadNextCard();
                }
            });
            t.setRepeats(false);
            t.start();
        }
    }

    private void endGame() {
        JOptionPane.showMessageDialog(this,
                "🎉 Game Complete!\nYour Score: " + score + " / " + cards.size(),
                "Finished",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        new ThaiFlashcardGame().setVisible(true);
    }
}
