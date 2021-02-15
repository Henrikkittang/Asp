package scanner;

/*
* The scanner takes a file path as the only argument and makes
* a 'LineNumberReader' object from the given file. The scanner starts on the call
* of the 'makeTokens' function. It then scans the file line by line, storing the
* result in the 'Tokens' ArrayList.
* The tokens the scanner is looking for are found in
* the 'TokenTypes' enum class.
*
*
* The errors throw by the scanner are: 'FileError', 'IndentError' and 'IllegalCharError'
* */


import com.company.Main;
import errorHandler.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static scanner.TokenTypes.*;


public class Scanner {
    private final List<Token> tokens = new ArrayList<>();
    private final Stack<Integer> indents = new Stack<>();
    private LineNumberReader lineReader = null;
    private String curLine = null;
    private int curPos = 0;
    private char curChar = '\0';
    private final int TABDIST = 4;

    public final String filename;


    public List<Token> getTokens(){ return tokens; }

    public Scanner(String fn){
        filename = fn;
        indents.push(0);

        // Makes a line reader object that reads the source file and keeps track of the line number
        try {
            lineReader =     new LineNumberReader(
                             new InputStreamReader(
                             new FileInputStream(filename), StandardCharsets.UTF_8));
        } catch (IOException e) {
            Main.error(new FileError("Cannot open file", filename, curPos, getLineNum()));
        }
        // lineReader.setLineNumber(1);
    }

    public void makeTokens(){
        while(lineReader != null){
            readNextLine();
            if (curLine != null && curLine.trim().length() > 0) {
                tokenizeLine();
                tokens.add(new Token(TokenTypes.NEWLINE, curPos, getLineNum()));
                Main.logger.noteToken(tokens.get(tokens.size() -1));
            }
        }
        tokens.add(new Token(EOF, curPos, getLineNum()));
        Main.logger.noteToken(tokens.get(tokens.size() -1));
    }

    private int getLineNum(){
        // Returns the current line number
        if(lineReader != null)
            return lineReader.getLineNumber();
        else
            return 0;
    }
    private void readNextLine(){
        // Reads the next line in the program, updates the correct variables
        // Sets curChar to '\0' and curLine to null, if the end of the file is reached

        String line = null;
        try {
            line = lineReader.readLine();
            if (line == null) {
                lineReader.close();
                lineReader = null;
                curChar = ' ';
                makeTabTokens();
            }
        } catch (IOException e) {
            lineReader = null;
            Main.error(new FileError("Unspecified file error", filename, curPos, getLineNum()));
        }
        curLine = line;
        curPos = 0;
    }

    private void readNextChar(){
        if(curPos < curLine.length()){
            curChar = curLine.charAt(curPos);
        }else{
            curChar = '\0';
        }
        curPos++;
    }

    private void tokenizeLine(){
        makeTabTokens();

        while (curChar != '\0'){
            if(Character.isWhitespace(curChar)){
                readNextChar();
                continue;
            }

            if(curChar == '#' || curChar == ';'){
                break;
            }if(Character.isLetter(curChar)){
                make_identifier();
            }else if( Character.isDigit(curChar) ){
                make_number();
            }else{
                make_symbol();
            }
        }
    }

    private void makeTabTokens(){
        int spaces = countLeadingWhitespace();
        if(spaces > indents.peek()){
            indents.push(spaces);
            tokens.add(new Token(INDENT, curPos, getLineNum()));
            Main.logger.noteToken(tokens.get(tokens.size() -1));
        }else{
            while(spaces < indents.peek()){
                indents.pop();
                tokens.add(new Token(DEDENT, curPos, getLineNum()));
                Main.logger.noteToken(tokens.get(tokens.size() -1));
            }
        }
        if(spaces % TABDIST != 0){
            Main.error(new IndentError("Spaces don`t match any indent level", filename, curPos, getLineNum()));
        }
    }

    private int countLeadingWhitespace(){
        int total = 0;
        for(int i = 0; i < curLine.length(); i++){
            readNextChar();
            if (curChar == '\t') {
                do {
                    total++;
                } while (total%TABDIST > 0);
            } else if (curChar == ' ') {
                total++;
            } else {
                // If the character is not tab char or whitespace, then exit
                break;
            }
        }
        return total;
    }

    private void make_identifier(){
        StringBuilder cur_str = new StringBuilder();

        while(Character.isLetter(curChar) || Character.isDigit(curChar)){
            cur_str.append(curChar);
            readNextChar();
        }

        String temp_str = cur_str.toString();
        if(temp_str.equals(TRUE.image)){
            Token newToken = new Token<Boolean>(BOOL, curPos, getLineNum());
            newToken.value = true;
            tokens.add(newToken);
            Main.logger.noteToken(tokens.get(tokens.size() -1));
            return;
        }if(temp_str.equals(FALSE.image)){
            Token newToken = new Token<Boolean>(BOOL, curPos, getLineNum());
            newToken.value = false;
            tokens.add(newToken);
            Main.logger.noteToken(tokens.get(tokens.size() -1));
            return;
        }

        for (TokenTypes tt : TokenTypes.values()) {
            if (tt.image.equals(temp_str)) {
                tokens.add(new Token(tt, curPos, getLineNum()));
                Main.logger.noteToken(tokens.get(tokens.size() -1));
                return;
            }
        }
        Token new_token = new Token(VARIABLE, curPos, getLineNum());
        new_token.variableName = temp_str;
        tokens.add(new_token);
        Main.logger.noteToken(tokens.get(tokens.size() -1));
    }

    private void make_number(){
        StringBuilder num_str = new StringBuilder();
        int dot_count = 0;

        while (Character.isDigit(curChar) ||  curChar == '.'){
            if(curChar == '.')
                dot_count++;

            if(dot_count > 1)
                Main.error(new IllegalCharError("Float can only contain one dot ('.')", filename, curPos, getLineNum()));

            num_str.append(curChar);
            readNextChar();
        }

        Token newToken;
        if(dot_count == 0){
            newToken = new Token<Long>(INT, curPos, getLineNum());
            newToken.value = Long.parseLong(num_str.toString());
        }else{
            newToken = new Token<Double>(FLOAT, curPos, getLineNum());
            newToken.value = Double.parseDouble(num_str.toString());
        }

        tokens.add(newToken);
        Main.logger.noteToken(tokens.get(tokens.size() -1));
    }

    private void make_symbol(){
        String str = "";
        while(!Character.isWhitespace(curChar) && !Character.isDigit(curChar) && !Character.isLetter(curChar) && (int)curChar != 0){
            str += curChar;
            readNextChar();
        }
        for(TokenTypes tt : TokenTypes.values()){
            if(tt.image.equals(str)){
                tokens.add(new Token(tt, curPos, getLineNum()));
                Main.logger.noteToken(tokens.get(tokens.size() -1));
                return;
            }
        }
        Main.error(new IllegalCharError("Symbol " + str + " is not a valid symbol", filename, curPos, getLineNum() ));
    }
}
