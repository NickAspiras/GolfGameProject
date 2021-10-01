import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;

public class Draw extends JPanel {
    public static int HOLE = 0;
    public static int XVAL = 0;
    public static int YVAL = 0;
    public static int HOLEX = 0;
    public static int HOLEY = 0;
    public static int NUMPTS = 0;
    public static int FAIRPTS = 0;
    public static int GREENPTS = 0;
    public static int OVERALLSCORE = 0;
    public static double DIST = 0.0;
    public static double REALDIST = 0.0;
    public static int[] BOUNDSX;
    public static int[] BOUNDSY;
    public static int[] FAIRX;
    public static int[] FAIRY;
    public static int[] GREENX;
    public static int[] GREENY;
    public static ArrayList<Polygon> BUNKERS;

    public static void main(String[] args) {
        JFrame frame = new JFrame("My Drawing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Draw canvas = new Draw();
        frame.add(canvas);
        frame.setSize(400, 400);
        frame.setVisible(true);
        Scanner input = new Scanner(System.in);
        String decision = "";
        String power = "";
        String angle = "";
        boolean bunker = false;
        boolean tour = false;
        int choice = 0;
        int counter = 1;
        System.out.println("Welcome to the Torrey Pines South Course in Torrey Pines, California");
        System.out.println("If you want to play the entire tour, type all. Otherwise, type select");//entire tour START HERE
        String beginning = input.nextLine().replaceAll("\\s", "");
        while(!beginning.equals("all") && !beginning.equals("select")){
            System.out.println("The following selection is invalid. Please re-enter your selection.");
            beginning = input.nextLine().replaceAll("\\s", "");
        }
        if(beginning.equals("all")){
            tour = true;
        }
        while(!decision.equals("quit")){
            if(tour){
                choice = counter;
                counter++;
            }
            else{
                do{
                    System.out.println("Which Hole would you like to play at?");
                    choice = Integer.parseInt(input.nextLine());
                }while (choice < 1 || choice > 18);
            }
            HOLE = choice;
            frame.repaint();
            int shots = 0;
            int[] holeValues = holeValue();
            int par = holeValues[0];
            double distance = holeValues[1];
            REALDIST = distance;
            double conversion = canvas.conversionFact(HOLE);
            Double shotDistance = 0.0;

            System.out.println("Welcome to Hole " + HOLE + " at Torrey Pines South Course");
            System.out.println("Say 'quit' at anytime to exit the game\n");

            System.out.println("Hole " + HOLE + " is a distance of " + Math.round(distance) + " with a par of " + par + "\n");
            while(distance > 1.0 && HOLEX != XVAL && HOLEY != YVAL){
                System.out.println("You are currently " + Math.round(distance) + " yards away from the hole.\n");
                System.out.println("You have the following clubs in your bag:");
                System.out.println(" CLUB             DISTANCE             _________________________________");
                System.out.println(" Driver           225-240 yards        |                90              |");
                System.out.println(" 3 Wood           210-225 yards        |         120           45       |");
                System.out.println(" 3 Iron           195-210 yards        |     135                   30   |");
                System.out.println(" 5 Iron           180-195 yards        |180                            0|");
                System.out.println(" 7 Iron           165-180 yards        |     210                   315  |");
                System.out.println(" 9 Iron           150-165 yards        |         220           300      |");
                System.out.println(" Pitching Wedge   135-150 yards        |_______________270______________|");
                System.out.println(" Sand Wedge       15-30    yards");
                System.out.println(" Putter           1-15    yards\n");
                System.out.println("Which club would you like to use?");

                decision = input.nextLine();
                if(decision.equals("quit")){
                    break;
                }
                while(canvas.distances(decision) == 0.0){
                    System.out.println("The following selection is not valid.");
                    System.out.println("Which club would you like to use?");
                    decision = input.nextLine();
                }
                if(bunker){
                    while(!decision.equals("Sand Wedge")){
                        System.out.println("You must use a Sand Wedge to get your ball back on the green");
                        System.out.println("Please re-enter your club choice as Sand Wedge to continue");
                        decision = input.nextLine();
                    }
                }

                Polygon bounds = new Polygon(BOUNDSX, BOUNDSY, NUMPTS);
                Polygon fairway = new Polygon(FAIRX, FAIRY, FAIRPTS);
                Polygon green = new Polygon(GREENX, GREENY, GREENPTS);

                System.out.println("Now choose the angle at which you want to shoot the ball in between 0 and 360 degrees");
                angle = input.nextLine();
                if(angle.equals("quit")){break;}
                while(Double.parseDouble(angle) < 0 || Double.parseDouble(angle) > 359){
                    System.out.println("The following angle is invalid. Please re-enter your distance using the gauge.");
                    angle = input.nextLine();
                }

                if(angle.equals("quit")){break;}
                Double angleVal = Double.parseDouble(angle);

                System.out.println("Now enter the distance using the spaces and the gauge below.");
                System.out.println(canvas.distances(decision) + "                         " + (15.0 + canvas.distances(decision)));
                System.out.println("|-----------------------------|");
                System.out.println("|     |     |     |     |     |");
                System.out.println("|-----------------------------|");
                power = input.nextLine();

                if(power.equals("quit")){break;}

                while(power.length() > 30){
                    System.out.println("The following distance is invalid. Please re-enter your distance using the gauge.");
                    System.out.println(canvas.distances(decision) + "                         " + (15.0 + canvas.distances(decision)));
                    System.out.println("|-----------------------------|");
                    System.out.println("|     |     |     |     |     |");
                    System.out.println("|-----------------------------|");
                    power = input.nextLine();
                }

                Double powerVal = (double)power.length() / 30.0;
                //Random rand = new Random();
                /*if(decision.equals("Putter") || decision.equals("Sand Wedge")){
                    System.out.println("You chose to use the " + decision +". Please enter the distance you would like to hit the ball");
                    shotDistance = Double.parseDouble(input.nextLine());
                    while((shotDistance > 15 && decision.equals("Putter")) || (shotDistance > 30 && decision.equals("Sand Wedge"))){
                        System.out.println("Your distance is either too far or invalid. Please enter the distance you would like to hit the ball");
                        shotDistance = Double.parseDouble(input.nextLine());
                    }
                }*/
                //else{}
                shotDistance =  canvas.distances(decision) + 15 * powerVal ;
                Double[] ratio = canvas.translationCalc(angleVal);
                System.out.println("You shot the ball " + shotDistance + " yards!\n\n\n");
                Point p = new Point((int)(XVAL + ratio[0] * shotDistance * conversion), (int)(YVAL -ratio[1] * shotDistance * conversion));
                if(!bounds.contains(p)){
                    System.out.println("You shot the ball out of bounds! You are awarded a penalty stroke and will shoot your next shot from the previous location\n");
                }
                else{
                    if(!decision.equals("Sand Wedge") && !decision.equals("Putter") && !fairway.contains(p) || !green.contains(p) || bunker){
                        shotDistance -= 2;
                    }
                    XVAL += ratio[0] * shotDistance * conversion;
                    YVAL -= ratio[1] * shotDistance * conversion;
                    System.out.println("XVAL: " + XVAL + " YVAL: " + YVAL);
                    System.out.println("Distance: " + canvas.ballDistance(p) * (REALDIST / DIST));
                    distance = (canvas.ballDistance(new Point(XVAL, YVAL)) * (REALDIST / DIST));//distance conversion to actual distance
                    distance = Math.abs(distance);//positive distance
                    bunker = false;

                    if(fairway.contains(XVAL, YVAL)){//shot on fairway
                        System.out.println("Good Shot! Your shot is on the fairway!\n");
                    }
                    else if(green.contains(XVAL, YVAL)){//shot on green
                        System.out.println("Great Shot! Your shot is on the green!\n");
                    }
                    else{//shot into bunker
                        for (Polygon polygon : BUNKERS) {//check to see if shot is in any bunkers.
                            System.out.println(polygon.contains(p));
                            if (polygon.contains(p)) {
                                bunker = true;
                                break;
                            }
                        }
                        if(bunker){//if bunker is true(current shot is in bunker)
                            System.out.println("Oh no! Your shot is in a bunker");
                            System.out.println("You must use a sand wedge to get your ball back on the green");
                        }
                        else{
                            System.out.println("Aw dang. Your shot is in the rough!\n");
                        }
                    }
                    if(distance < 1.0){
                        XVAL = HOLEX;
                        YVAL = HOLEY;
                    }
                    canvas.repaint();
                }
                shots += 1;
            }
            OVERALLSCORE += (shots - par);
            System.out.println("Your overall score is " + OVERALLSCORE);
            if(shots == 1){
                break;
            }
            if((shots - par) > 3){
                System.out.println("You got a +" + (shots - par) + ".\n" + "Better luck next time!");
            }
            else{
                String scoreStr = canvas.score(shots - par);
                System.out.println("You got a " + scoreStr + ".");
                if((shots - par) < 1){
                    System.out.println("Great Job! Way to navigate the greens!");
                }
                else{
                    System.out.println("You are almost there! Try better next hole!");
                }
            }
            System.out.println("Would you like to play another hole? Say 'quit' to leave or say 'continue' to continue.");
            String check = input.nextLine();
            while(!check.equals("quit") && !check.equals("continue")){
                System.out.println("The following choice is invalid. Please re-enter your choice.");
                check = input.nextLine();
            }
        }
        System.out.println("Thank you for playing Torrey Pines South Course! See you next time!");
        System.out.println("Your final score was " + OVERALLSCORE);
        System.exit(0);
    }


    //HashMap search to setup the ball on the tee to begin and the par / real distance to the hole
    public static int[] holeValue(){
        HashMap<Integer, int[]> holeVals = new HashMap<>();
        HashMap<Integer, int[]> ballPlace = new HashMap<>();
        int[] par = new int[]{4,450};
        int[] place = new int[]{170, 345};
        holeVals.put(1, par);
        ballPlace.put(1, place);
        par = new int[]{4,389};
        place = new int[]{200, 335};
        holeVals.put(2, par);
        ballPlace.put(2, place);
        par = new int[]{3, 200};
        place = new int[]{255, 315};
        holeVals.put(3, par);
        ballPlace.put(3, place);
        XVAL = ballPlace.get(HOLE)[0];
        YVAL = ballPlace.get(HOLE)[1];
        return holeVals.get(HOLE);
    }

    //Conversion to obtain the factor to move the ball at a certain angle
    public Double[] translationCalc(Double degrees){
        Double[] ratio = new Double[2];
        ratio[0] = Math.cos(Math.toRadians(degrees));
        ratio[1] = Math.sin(Math.toRadians(degrees));
        return ratio;
    }

    //HashMap to obtain the distances for each club
    public Double distances(String club){
        HashMap<String, Double> distance = new HashMap<>();
        distance.put("Driver", 225.0);
        distance.put("3 Wood", 210.0);
        distance.put("3 Iron", 195.0);
        distance.put("5 Iron", 180.0);
        distance.put("7 Iron", 165.0);
        distance.put("9 Iron", 150.0);
        distance.put("Pitching Wedge", 135.0);
        distance.put("Sand Wedge", 15.0);
        distance.put("Putter", 1.0);
        if(!distance.containsKey(club)){
            return 0.0;
        }
        return distance.get(club);
    }

    public Double ballDistance(Point p){
        return Math.sqrt(Math.pow(HOLEY- p.getY(), 2) + Math.pow(HOLEX - p.getX(), 2));
    }

    //HashMap to search for the conversion factor for each hole
    public Double conversionFact(Integer hole){
        HashMap<Integer, Double> ratio = new HashMap<>();
        HashMap<Integer, Double> dist = new HashMap<>();
        ratio.put(1, 0.7381123733);//dist: 332.150568, actdist: 450
        dist.put(1, 332.150568);
        ratio.put(2, 0.823625673521851);//dist: 320.390387, actdist: 389
        dist.put(2, 320.390387);
        ratio.put(3, 1.38856941);//dist: 277.713882, actdist: 200
        dist.put(3, 277.713882);
        DIST = dist.get(hole);
        return ratio.get(hole);
    }

    //HashMap to search for the score name for each score
    public String score(Integer val){
        HashMap<Integer, String> totalScore = new HashMap<>();
        totalScore.put(-3, "Albatross");
        totalScore.put(-2, "Eagle");
        totalScore.put(-1, "Birdie");
        totalScore.put(0, "Par");
        totalScore.put(1, "Bogey");
        totalScore.put(2, "Double Bogey");
        totalScore.put(3, "Triple Bogey");
        return totalScore.get(val);
    }

    //Primary paint method
    public void paint(Graphics g) {
        super.paint(g);
        this.setBackground(Color.WHITE);
        int counter = HOLE;

        Color sand = new Color(237,201, 175);
        Color fairway = new Color(38, 98, 63);
        Color rough = new Color(7, 69, 0);
        Color green = new Color(0, 135, 99);

        switch(counter){
            case 1:
                //hole 1
                g.setColor(rough);
                g.fillRect(100, 0, 150, 400);
                int[] xvals = new int[]{100, 240, 240, 100};
                int[] yvals = new int[]{0, 0, 350, 350};
                BOUNDSX = xvals;
                BOUNDSY = yvals;
                NUMPTS = 4;
                g.setColor(fairway);
                g.fillOval(157, 325, 40, 40);//teebox1
                g.fillOval(160, 320, 35, 35);//teebox2
                int[] xfair = new int[]{170, 190, 195, 195, 170, 150, 150, 155};
                int[] yfair = new int[]{295, 255, 220, 120, 70, 120, 220, 250};
                g.fillPolygon(xfair, yfair, xfair.length);
                FAIRX = xfair;
                FAIRY = yfair;
                FAIRPTS = xfair.length;
                g.setColor(sand);
                ArrayList<Polygon> bunkers = new ArrayList<>();
                int[] xsand1 = new int[]{89, 74, 65, 76};
                int[] ysand1 = new int[]{146, 135, 144, 156};
                int[] xsand2 = new int[]{65, 61, 40, 39};
                int[] ysand2 = new int[]{73, 63, 62, 73};
                int[] xsand3 = new int[]{101, 98, 77, 75};
                int[] ysand3 = new int[]{72, 62, 63, 72};
                for(int i = 0; i < xsand1.length; i++){
                    xsand1[i] = xsand1[i] + 50;
                    ysand1[i] = ysand1[i] + 15;
                    xsand2[i] = xsand2[i] + 100;
                    ysand2[i] = ysand2[i] - 15;
                    xsand3[i] = xsand3[i] + 100;
                    ysand3[i] = ysand3[i] - 15;
                }
                Polygon set = new Polygon(xsand1, ysand1, xsand1.length);
                g.fillPolygon(set);//sandtrap1
                bunkers.add(set);
                set = new Polygon(xsand2, ysand2, xsand2.length);
                g.fillPolygon(set);//sandtrap2
                bunkers.add(set);
                set = new Polygon(xsand3, ysand3, xsand3.length);
                g.fillPolygon(set);//sandtrap3
                bunkers.add(set);
                BUNKERS = bunkers;
                g.setColor(green);
                int[] xgreen = new int[]{175, 160, 150, 160, 175, 190, 195, 190};
                int[] ygreen = new int[]{0, 5, 20, 35, 40, 35, 20, 5};
                g.fillPolygon(xgreen, ygreen, xgreen.length);
                GREENX = xgreen;
                GREENY = ygreen;
                GREENPTS = xgreen.length;
                g.setColor(Color.BLACK);
                HOLEX = 160;
                HOLEY = 13;
                g.fillOval(HOLEX, HOLEY, 4, 4);
                g.setColor(Color.WHITE);
                g.fillOval(XVAL, YVAL, 4, 4);
                break;
            case 2:
                //hole2
                xvals = new int[]{152, 170, 180, 177, 177, 145, 64, 29, 14, 2, 5, 29, 62, 85, 86, 79, 80, 92, 96, 105, 129};
                yvals = new int[]{346, 341, 325, 260, 169, 129, 9, 8, 13, 30, 69, 100, 135, 158, 171, 196, 218, 251, 248, 279, 326};
                for(int i = 0; i < xvals.length; i++){
                    xvals[i] = xvals[i] + 50;
                    yvals[i] = yvals[i] + 15;
                }
                BOUNDSX = xvals;
                BOUNDSY = yvals;
                NUMPTS = xvals.length;
                xfair = new int[]{140, 148, 146, 139, 132, 128, 95, 95, 60, 52, 39, 46, 82, 100, 107, 126};
                yfair = new int[]{283, 274, 240, 198, 156, 140, 91, 89, 48, 43, 49, 68, 111, 139, 160, 275};
                for(int i = 0; i < xfair.length; i++){
                    xfair[i] = xfair[i] + 55;
                    yfair[i] = yfair[i] + 15;
                }
                FAIRX = xfair;
                FAIRY = yfair;
                FAIRPTS = xfair.length;
                xgreen = new int[]{25, 15, 13, 31, 34, 43, 48, 37, 35};
                ygreen = new int[]{20, 24, 32, 39, 44, 41, 35, 31, 20};
                for(int i = 0; i < xgreen.length; i++){
                    xgreen[i] = xgreen[i] + 55;
                    ygreen[i] = ygreen[i] + 15;
                }
                GREENX = xgreen;
                GREENY = ygreen;
                GREENPTS = xgreen.length;
                bunkers = new ArrayList<>();
                xsand1 = new int[]{39, 45, 41, 34, 32};
                ysand1 = new int[]{66, 56, 50, 50, 60};
                xsand2 = new int[]{29, 31, 30, 23, 22};
                ysand2 = new int[]{54, 47, 39, 43, 53};
                for(int i = 0; i < xsand1.length; i++){
                    xsand1[i] = xsand1[i] + 40;
                    ysand1[i] = ysand1[i] + 10;
                    xsand2[i] = xsand2[i] + 40;
                    ysand2[i] = ysand2[i] + 10;
                }
                g.setColor(rough);
                g.fillPolygon(xvals, yvals, xvals.length);
                g.setColor(sand);
                set = new Polygon(xsand1, ysand1, xsand1.length);
                bunkers.add(set);
                g.fillPolygon(set);
                set = new Polygon(xsand2, ysand2, xsand2.length);
                bunkers.add(set);
                g.fillPolygon(set);
                BUNKERS = bunkers;
                g.setColor(fairway);
                g.fillPolygon(xfair, yfair, xfair.length);
                g.setColor(green);
                g.fillOval(190, 320, 30, 30);//teebox1
                g.fillOval(193, 315, 25, 25);//teebox2
                g.fillPolygon(xgreen, ygreen, xgreen.length);//green
                g.setColor(Color.BLACK);
                HOLEX = 75;
                HOLEY = 40;
                g.fillOval(HOLEX, HOLEY, 7, 7);
                g.setColor(Color.WHITE);
                g.fillOval(XVAL, YVAL, 7, 7);
                break;
            case 3:
                //hole2
                xvals = new int[]{264, 280, 254, 237, 214, 195, 176, 146, 67, 62, 82, 120, 140, 168, 168, 204, 241};
                yvals = new int[]{329, 312, 278, 212, 130, 70, 20, 16, 64, 94, 120, 134, 197, 232, 278, 314, 327};
                for(int i = 0; i < xvals.length; i++){
                    xvals[i] = xvals[i] + 20;
                    yvals[i] = yvals[i] + 15;
                }
                BOUNDSX = xvals;
                BOUNDSY = yvals;
                NUMPTS = xvals.length;
                xfair = new int[]{208, 212, 204, 188, 185, 174, 158, 148, 145, 153, 169, 182, 190};
                yfair = new int[]{222, 203, 178, 140, 116, 100, 95, 104, 125, 153, 187, 212, 226};
                for(int i = 0; i < xfair.length; i++){
                    xfair[i] = xfair[i] + 20;
                    yfair[i] = yfair[i] + 15;
                }
                FAIRX = xfair;
                FAIRY = yfair;
                FAIRPTS = xfair.length;
                xgreen = new int[]{160, 163, 159, 146, 130, 118, 129, 146, 148};
                ygreen = new int[]{89, 71, 51, 34, 36, 45, 61, 77, 89};
                for(int i = 0; i < xgreen.length; i++){
                    xgreen[i] = xgreen[i] + 20;
                    ygreen[i] = ygreen[i] + 15;
                }
                GREENX = xgreen;
                GREENY = ygreen;
                GREENPTS = xgreen.length;
                bunkers = new ArrayList<>();
                int[] xsand = new int[]{140, 140, 128, 112, 112, 124, 124};
                int[] ysand = new int[]{92, 74, 69, 69, 81, 81, 92};
                for(int i = 0; i < xsand.length; i++){
                    xsand[i] = xsand[i] + 20;
                    ysand[i] = ysand[i] + 15;
                }
                g.setColor(rough);
                g.fillPolygon(xvals, yvals, xvals.length);
                g.setColor(fairway);
                g.fillPolygon(xfair, yfair, xfair.length);
                g.setColor(green);
                g.fillOval(240, 300, 30, 30);//teebox1
                g.fillOval(248, 305, 25, 25);//teebox2
                g.fillPolygon(xgreen, ygreen, xgreen.length);//green
                g.setColor(sand);
                set = new Polygon(xsand, ysand, xsand.length);
                bunkers.add(set);
                g.fillPolygon(set);
                g.setColor(Color.BLACK);
                HOLEX = 145;
                HOLEY = 60;
                g.fillOval(HOLEX, HOLEY, 7, 7);
                g.setColor(Color.WHITE);
                g.fillOval(XVAL, YVAL, 7, 7);
                break;
            default:

        }



    }
}
