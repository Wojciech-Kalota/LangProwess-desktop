package me.regos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


public class MainMenu extends JFrame implements ActionListener{

    char breaker = ';';
    char ender = '\n';
    Pair<String, String> p_tmp = new Pair();
    Queue<Pair<String, String>> list = new LinkedList<>();
    boolean correctness = false;
    boolean swap = false;
    FileReader reader;
    int data;

    int n = 0;

    JButton saveButton;
    JButton nextButton;
    JButton iWasCorrectButton;
    JButton swapLanguagesButton;
    JTextField textField;
    JLabel definitionLabel;
    JLabel correctnessLabel;
    JLabel correctAnswerLabel;
    JLabel versionLabel;
    JLabel wordCountLabel;
    JLabel infoLabel;

    File file;

    MainMenu(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Poor Man's Quizlet");
        this.setSize(800, 400);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        versionLabel = new JLabel("ver: 1.0.0");
        versionLabel.setBounds(10, 10, 200, 50);
        versionLabel.setVerticalAlignment(JLabel.TOP);
        versionLabel.setHorizontalAlignment(JLabel.LEFT);
        wordCountLabel = new JLabel("Words left: " + n);
        wordCountLabel.setBounds(580, 10, 200, 50);
        wordCountLabel.setVerticalAlignment(JLabel.TOP);
        wordCountLabel.setHorizontalAlignment(JLabel.RIGHT);
        infoLabel = new JLabel("Info");
        infoLabel.setBounds(0, 300, 800, 50);
        infoLabel.setVerticalAlignment(JLabel.CENTER);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);


        saveButton = new JButton("Save");
        saveButton.setBounds(50, 250, 150, 50);
        saveButton.setFocusPainted(false);
        nextButton = new JButton("Next");
        nextButton.setBounds(600, 150, 150, 50);
        nextButton.setFocusPainted(false);
        iWasCorrectButton = new JButton("I was correct");
        iWasCorrectButton.setBounds(600, 250, 150, 50);
        iWasCorrectButton.setFocusPainted(false);
        swapLanguagesButton = new JButton("Swap languages");
        swapLanguagesButton.setBounds(50, 150, 150, 50);
        swapLanguagesButton.setFocusPainted(false);

        textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setText("Type your answers here");
        textField.setBounds(250, 150, 300, 50);
        textField.setFont(new Font("Arial", Font.PLAIN, 20));

        definitionLabel = new JLabel();
        definitionLabel.setBounds(00, 50, 800, 150);
        definitionLabel.setFont(new Font("Arial", Font.BOLD, 40));
        definitionLabel.setText("Definition");
        definitionLabel.setVerticalAlignment(JLabel.TOP);
        definitionLabel.setHorizontalAlignment(JLabel.CENTER);
        correctnessLabel = new JLabel();
        correctnessLabel.setBounds(250, 200, 300, 50);
        correctnessLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        correctnessLabel.setVerticalAlignment(JLabel.CENTER);
        correctnessLabel.setHorizontalAlignment(JLabel.CENTER);
        correctnessLabel.setText("Correct/Incorrect");
        correctAnswerLabel = new JLabel();
        correctAnswerLabel.setBounds(250, 250, 300, 50);
        correctAnswerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        correctAnswerLabel.setVerticalAlignment(JLabel.CENTER);
        correctAnswerLabel.setHorizontalAlignment(JLabel.CENTER);
        correctAnswerLabel.setText("Answer");

        saveButton.addActionListener(this::actionPerformed);
        nextButton.addActionListener(this::actionPerformed);
        swapLanguagesButton.addActionListener(this::actionPerformed);
        iWasCorrectButton.addActionListener(this::actionPerformed);
        textField.addActionListener(this::actionPerformed);

        this.add(infoLabel);
        this.add(versionLabel);
        this.add(wordCountLabel);
        this.add(saveButton);
        this.add(nextButton);
        this.add(iWasCorrectButton);
        this.add(swapLanguagesButton);
        this.add(textField);
        this.add(definitionLabel);
        this.add(correctnessLabel);
        this.add(correctAnswerLabel);
        this.setVisible(true);


        file = new File("toLearn.txt");

        if(!file.exists() || file.length() == 0){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            file = new File("data.txt");
        }


        try {
            reader = new FileReader(file.getName(), StandardCharsets.UTF_8);
            data = reader.read();


            while(data != -1){

                p_tmp.setFirst(wordBuilder(breaker));
                data = reader.read();
                p_tmp.setSecond(wordBuilder(ender));
                data = reader.read();
                list.add(p_tmp);
                n++;
                p_tmp = new Pair<>();

            }
            reader.close();
            file = new File("toLearn.txt");




        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String wordBuilder(char stopSign){

        String tmp = "";
        while(data != stopSign && data != -1) {
            try {
                tmp += (char)data;
                data = reader.read();
            } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tmp;
        }

    private void nextWord(){

        iWasCorrectButton.setEnabled(false);
        correctAnswerLabel.setVisible(false);
        correctnessLabel.setVisible(false);
        textField.setText("");
        correctnessLabel.setText("");
        infoLabel.setText("");
        textField.requestFocus();
        textField.setEditable(true);

        if(!correctness){
            list.add(list.peek());
        }else {
            correctness = false;
            n--;
        }
        list.remove();

        wordCountLabel.setText("Words left: " + n);

        if(!swap) {
            definitionLabel.setText(list.peek().getFirst());
            correctAnswerLabel.setText(list.peek().getSecond());
        }else{
            definitionLabel.setText(list.peek().getSecond());
            correctAnswerLabel.setText(list.peek().getFirst());
        }
    }

    private void updateFile(){
        textField.requestFocus();
        try {
            FileWriter writer = new FileWriter(file.getName());
            for(Pair item: list){

                writer.write((String) item.getFirst());
                writer.write(breaker);
                writer.write((String) item.getSecond());
            }
            infoLabel.setText("Progress saved");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void swapLanguages(){
        textField.requestFocus();
        swap = !swap;
        infoLabel.setText("Languages will be swapped from now on");
    }

    private void iWasCorrect(){
        textField.requestFocus();
        textField.setEditable(false);
        correctnessLabel.setText("Correct");
        correctness = true;
        infoLabel.setText("Answer accepted");
    }

    private void onCheck(){

        if(correctAnswerLabel.isVisible()){
            nextWord();
        } else {
            textField.setEditable(false);
            correctAnswerLabel.setVisible(true);
            correctnessLabel.setVisible(true);

            if (textField.getText().strip().equals(correctAnswerLabel.getText().strip())) {
                correctnessLabel.setText("Correct");
                correctness = true;
            } else {
                correctnessLabel.setText("Incorrect");
                iWasCorrectButton.setEnabled(true);
                correctness = false;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() ==  nextButton) {
            nextWord();
        } else if (e.getSource() ==  iWasCorrectButton) {
            iWasCorrect();
        } else if (e.getSource() ==  swapLanguagesButton) {
            swapLanguages();
        } else if (e.getSource() ==  saveButton) {
            updateFile();
        } else if (e.getSource() == textField) {
            onCheck();
        }
    }

}
