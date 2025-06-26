package com.example.manhinhappmusic.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LyricLine {
    private long time;
    private String text;

    public static List<LyricLine> parseLrc(InputStream inputStream) throws IOException
    {
        List<LyricLine> lyricLines = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            Matcher matcher = Pattern.compile("\\[(\\d{2}):(\\d{2}).(\\d{2})\\]").matcher(line);
            while (matcher.find())
            {
                int minutes = Integer.parseInt(matcher.group(1));
                int seconds = Integer.parseInt(matcher.group(2));
                int hundredths = Integer.parseInt(matcher.group(3));
                long milliseconds = minutes * 60_000 + seconds * 1000 + hundredths * 10;
                String text = line.substring(matcher.end()).trim();
                lyricLines.add(new LyricLine(milliseconds, text));
            }
        }
        return lyricLines;
    }
    public static List<String> parseToStrings(InputStream inputStream) throws IOException
    {
        List<String> lyrics = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            Matcher matcher = Pattern.compile("\\[(\\d{2}):(\\d{2}).(\\d{2})\\]").matcher(line);
            while (matcher.find())
            {
                String text = line.substring(matcher.end()).trim();
                lyrics.add(text);
            }
        }
        return lyrics;
    }

    public static List<String> parseToStrings(List<LyricLine> lyricLines)
    {
        List<String> lyrics = new ArrayList<>();
        for(LyricLine lyricLine : lyricLines)
        {
            lyrics.add(lyricLine.text);
        }
        return  lyrics;
    }
}
