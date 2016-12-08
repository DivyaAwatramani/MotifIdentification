import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Divya on 12/6/2016.
 */


public class MotifIdentification {
    String sequence;
    StringBuffer numSequence;
    Map<BigInteger, Integer> scoreMap;
    BigInteger motifLength;
    Map<BigInteger, List<Integer>> startIndexMap;

    MotifIdentification() {
        scoreMap = new HashMap<>();
        sequence = "";
        numSequence = new StringBuffer();
        startIndexMap = new HashMap<>();
    }

    void readFile(String filenname){
        try(BufferedReader br = new BufferedReader(new FileReader("C:/Users/Divya/IdeaProjects/untitled/"+filenname))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            sequence = sb.toString();
            System.out.println("THE DNA SEQUNECE IS "+ sequence);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void generateMotif(int motifLen) {

        for(int i =0;i < motifLength.intValue() ; i++)
                window_Slider(i);
    }
    boolean checkOverlappingSeq(List<Integer> startList, int start){
        int i=0;
        while(i < startList.size()){
            int seqStart = startList.get(i);
            int seqEnd = startList.get(i)+ motifLength.intValue() - 1;
            if((seqStart <= start  && start <= seqEnd ) || (start <= seqStart && seqStart <= start + motifLength.intValue() - 1 ))
                return true;
            i++;
        }
        return false;
    }

    BigInteger getWindowScore(String window, int start) {
        int i =0;
        BigInteger score = new BigInteger("0");
        System.out.println("The Window -->"+ window);
        while (i < window.length()) {
            System.out.println( BigInteger.valueOf(Long.parseLong(""+window.charAt(i))));
            BigInteger value = BigInteger.valueOf(Long.parseLong(""+window.charAt(i)));
            score = score.add(value.multiply(motifLength.pow(motifLength.intValue()-i+1)));
            i++;
        }
        if(startIndexMap.get(score) != null) {

            if(!checkOverlappingSeq(startIndexMap.get(score),start)) {

                if (scoreMap.get(score) != null) {
                    int val = scoreMap.get(score) + 1;
                    scoreMap.put(score, val);
                } else {
                    scoreMap.put(score, 1);
                    //startIndexMap.put(score,)
                }
                startIndexMap.get(score).add(start);
            }

        }
        else
        {
            List<Integer> startList = new ArrayList<>();
            startList.add(start);
            startIndexMap.put(score,startList);
            scoreMap.put(score, 1);
        }
        return score;
    }

    void window_Slider(int windowStarter) {
        int i = 0;
        int numOfWindows = numSequence.substring(windowStarter).length()/motifLength.intValue();
        int start = windowStarter;
        while (numOfWindows > 0) {
            String window =numSequence.substring(start, start + motifLength.intValue());
            System.out.println("Start position: " + start);
            BigInteger score = getWindowScore(window,start);
            System.out.println("THE SCORE "+ score);
            i++;
            start = windowStarter + (i * motifLength.intValue());
            numOfWindows--;
        }

    }
//    BigInteger quinaryEquivalent(int score, int motifLength) {
//        List<Integer> remainder = new ArrayList<>();
//
//        int count = 0;
//        String result = "";
//        while(score!= 0 ) {
//            remainder.add( count, score % motifLength != 0 ? score % motifLength : 0 );
//            score /= motifLength;
//            try {
//                result += remainder.get( count );
//            } catch( NumberFormatException e ) {
//                e.printStackTrace();
//            }
//        }
//        return new BigInteger( new StringBuffer( result ).reverse().toString() );
//    }

    void symbol_to_numeric() {
        int i = 0;
         numSequence = new StringBuffer();
        while(i< sequence.length()) {
            if (sequence.charAt(i) == 'A')
                numSequence.append(new Character('1').toString());
            else if (sequence.charAt(i) == 'T')
                numSequence.append(new Character('2').toString());
            else if (sequence.charAt(i) == 'C')
                numSequence.append(new Character('3').toString());
            else if (sequence.charAt(i) == 'G')
                numSequence.append(new Character('4').toString());
            i++;
        }
        System.out.println("THE NUMBER SEQUENCE IS"+ numSequence);
    }

    Stack<Integer> quinaryEquivalent(BigInteger score, BigInteger motifLength) {
        BigInteger number = score.divide((motifLength.multiply(motifLength)));
        Stack<Integer> motif = new Stack<>();
        int i = 0;
        int seq = 0;
        while (number.intValue() != 0) {
            motif.push(number.remainder(motifLength).intValue());
            //seq += Math.pow(10.0, i++*1.0) * number%motifLength;
            number = number.divide(motifLength);
        }
        System.out.println("score "+ score);
        System.out.println("motif " + motif);
        System.out.println("sequence "+ seq);
        return motif;
    }


    StringBuffer convertToPattern(Stack<Integer> pattern) {
        StringBuffer motif = new StringBuffer();
        while(!pattern.isEmpty() ) {
            switch (pattern.pop()) {
                case 1: motif.append("A"); break;
                case 2: motif.append("T"); break;
                case 3: motif.append("C"); break;
                case 4: motif.append("G"); break;
            }
        }
        return motif;
    }

    BigInteger getMostFrequentScore() {
       Iterator itr = scoreMap.entrySet().iterator();
        int max = -1;
        BigInteger maxScore = new BigInteger("0");
        while (itr.hasNext()) {
           Map.Entry entry =(Map.Entry)itr.next();
            if((int)entry.getValue() > max) {
                max = (int) entry.getValue();
                maxScore = (BigInteger)entry.getKey();
            }
        }
        return maxScore;
    }
    public static void main(String[] args) {
        MotifIdentification motifIdentification = new MotifIdentification();
        motifIdentification.motifLength = new BigInteger("12");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" Enter the number of sequences");
        long starttime= 0L;
        try {
            int num = Integer.parseInt(br.readLine());
            for(int i =1; i <= num ;i++) {
                starttime = System.currentTimeMillis();
                motifIdentification.readFile("file"+i+".txt");
                motifIdentification.symbol_to_numeric();
                motifIdentification.generateMotif(motifIdentification.motifLength.intValue());
                System.out.println(motifIdentification.scoreMap);
                motifIdentification.sequence = "";
            }
            Iterator itr = motifIdentification.scoreMap.keySet().iterator();
            BigInteger frequentScore = motifIdentification.getMostFrequentScore();
            System.out.println(frequentScore);
             //  System.out.println(motifIdentification.quinaryEquivalent(frequentScore, 5));
          Iterator itr1 =  motifIdentification.scoreMap.entrySet().iterator();
            while (itr1.hasNext()) {
                Map.Entry entry = (Map.Entry)itr1.next();
                if((int)entry.getValue() > 1)
                System.out.println(motifIdentification.convertToPattern(motifIdentification.quinaryEquivalent((BigInteger)entry.getKey(), motifIdentification.motifLength)));
            }
            System.out.println("MOST FREQUENT SCORE IS "+ frequentScore);
            System.out.println(motifIdentification.convertToPattern(motifIdentification.quinaryEquivalent(frequentScore, motifIdentification.motifLength)));
            System.out.println("TIME TAKEN "+ ((System.currentTimeMillis()- starttime)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
