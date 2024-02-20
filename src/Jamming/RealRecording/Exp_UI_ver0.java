//package Jamming.RealRecording;
//
///**
// * Created by Roi_Yozevitch on 7/16/2017.
// */
//
//import Geometry.Point2D;
//import Jamming.*;
//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.scene.Scene;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.control.Label;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import static javafx.geometry.Pos.CENTER;
//
//
//
//
//public class Exp_UI_ver0 extends Application {
//
//      double Image_x_size_meters;
//      double Image_y_size_meters;
//      double factorX;
//      double factorY ;
//    //static   double minXImageUtm =708216.4159;
//    //static  double minYImageUtm = 3553913.0337;
//    //static String fileName;
//    //static   double maxXImageUtm =708712.9605;
//    //static  double maxYImageUtm = 3554175.90585;
//
//       double minXImageUtm;//  708216.4159;
//      double minYImageUtm ;
//     String fileName;
//       double maxXImageUtm ;
//      double maxYImageUtm ;
//     //   1419082529
//         //       1551627827
//
//    static  long UTC_Start_Time_First_Jam_Record;// = 15018190;
//    static  long UTC_End_Time_First_Jam_Record;// = UTC_Start_Time_First_Jam_Record + 60*10*1000;
//    static  String imagePath;
//
//
//    static double current_UTC_time;// = UTC_Start_Time_First_Jam_Record;
//
//
//    public  void loadDataForArielExp()
//    {
//       Image_x_size_meters = 496.5;
//       Image_y_size_meters = 263;
//       factorX=3;
//       factorY = 3;
//       minXImageUtm =708216.4159;
//       minYImageUtm = 3553913.0337;
//       maxXImageUtm =708712.9605;
//       maxYImageUtm = 3554175.90585;
//       UTC_Start_Time_First_Jam_Record = 1857319;
//       UTC_End_Time_First_Jam_Record = 1857689;
//       imagePath = "Ariel3.jpg";
//       fileName = "finalRecords.csv";
//    }
//    public void loadDataForSouthArmyExp()
//    {
//
//        //Image_x_size_meters = 608.6;
//        //Image_y_size_meters = 436.5;
//        factorX = 3.0125;
//        factorY = 2.862;
//        minXImageUtm =645978;
//        minYImageUtm = 3448609;
//
//        maxXImageUtm =646456;
//        maxYImageUtm = 3448892;
//        Image_x_size_meters = maxXImageUtm-minXImageUtm;
//        Image_y_size_meters  = maxYImageUtm-minYImageUtm;
//                                    //1.51056389392   1510134748   1510567091  1510565148564.00  1510566785
//        System.out.println(Image_x_size_meters+ " "+Image_y_size_meters);
//        UTC_Start_Time_First_Jam_Record =   1510566800;//8th November 9:52
//       // UTC_Start_Time_First_Jam_Record = 1501819200;  //150,181,9701 150,181,99
//        UTC_End_Time_First_Jam_Record = UTC_Start_Time_First_Jam_Record+60*10*1000;
//        //imagePath = "expSud.png";
//        //imagePath="MalaMap.png";
//        imagePath = "MalaFlip2.png";
//        //imagePath="malaMapUpsideDown.png";
//
//        // = "recordsSout2.csv";
//        fileName = "finalExperiment2.csv";
//        //imagePath = "Ariel3.jpg";
//    }
//
//
//    JamParticles particle_list;
//    final ImageView pic = new ImageView();
//    final Label name = new Label();
//    final Label binName = new Label();
//    final Label description = new Label();
//
//    public Point2D convertUTMtoScreenCOrdinates(double utmx, double utmY)
//    {
//        double tmpX = factorX*(utmx- minXImageUtm);
//       // double tmpy =factorY*( maxYImageUtm-utmY);
//        double tmpy =factorY*( utmY - minYImageUtm);
//        return new Point2D(tmpX,tmpy);
//
//    }
//
//    private int[][] InitHeatmap(int xSize, int ySize) {
//        int[][] heatMap = new int[xSize][ySize];
//        for (int i=0;i<(int)xSize;i++)
//            for(int j=0;j<ySize;j++)
//                heatMap[i][j]=0;
//
//        return heatMap;
//    }
//
//    public int[][] addPoint(int[][] heatmap, RealClient tmp, Point2D points, int xSize, int ySize) {
//        double maxSnr = tmp.getMaxSnrGPS();
//        int [][] heatMapTmp = InitHeatmap(xSize,ySize);
//        double absMaxSnr = 37;
//        double Radius = getRadiusFromSnr(maxSnr, absMaxSnr);
//        double x = points.getX();
//        double y = points.getY();
//       // System.out.println(Radius+ "  ..."+x+ "    "+y);
//        double thresHold=5;
//        //double Rr=Radius;
//        for(double Rr = Radius-thresHold;Rr<(Radius+thresHold);Rr++)
//            for (double angle=0;angle<2*Math.PI;angle+=0.1) {
//                int newX = (int) (x+Rr*Math.cos(angle));
//                int newY = (int) (y+Rr*Math.sin(angle));
//            //    System.out.println(newX+ "  "+newY);
//                if(newX<xSize && newY<ySize && newX>=0 && newY>=0)
//                    if (heatMapTmp[newX][newY]==0)
//                        heatMapTmp[newX][newY]=1;
//
//            }
//
//        for (int i=0;i<xSize;i++)
//            for(int j=0;j<ySize;j++)
//                heatmap[i][j]+=heatMapTmp[i][j];
//        return heatmap;
//
//
//
//    }
//
//
//    public Point2D convertUTMtoScreenCOrdinatesWithRotation(double utmx, double utmY, double angle)
//    {
//        angle = Math.toRadians(angle);
//
//        double tmpX = factorX*(utmx- minXImageUtm);
//        // double tmpy =factorY*( maxYImageUtm-utmY);
//        double tmpy =Image_y_size_meters*factorY- factorY*( utmY - minYImageUtm);
//        double newX = tmpX*Math.cos(angle)-tmpy*Math.sin(angle);
//        double newY = tmpX*Math.sin(angle)+ tmpy*Math.cos(angle);
//      //  System.out.println(newX+ " "+newY   +" Old Values "+tmpX+ " "+tmpy);
//        return new Point2D(newX,newY);
//
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//    static int client;
//
//    @Override
//    public void start(Stage stage) throws IOException {
//
//
//       //loadDataForArielExp();
//        loadDataForSouthArmyExp();
//         current_UTC_time = UTC_Start_Time_First_Jam_Record;
//         //current_UTC_time =  1521973943136.0;
//        current_UTC_time = 15219736087.0;
//
//
//        int xSize = (int) (factorX*Image_x_size_meters);
//        int ySize = (int) (factorY*Image_y_size_meters);
//        System.out.println(xSize+ " "+ ySize);
//        this.particle_list  = new JamParticles();
//        int[][] tr = InitHeatmap(xSize, ySize);
//        System.out.println(tr.length);
//        System.out.println(tr[0].length);
//
//
//        particle_list.InitWithVelocity(minXImageUtm, minYImageUtm, maxXImageUtm, maxYImageUtm);
//        client=0;
//
//
//        //RealClientList client = TestMain.Parse3(0,0 ,"D:\\GoogleDrive\\Roi PhD\\JammingMafat2018\\src\\Jamming\\gnss_status_2018_03_25_13_23_39_log.csv", "D:\\GoogleDrive\\Roi PhD\\JammingMafat2018\\src\\Jamming\\location_2018_03_25_13_23_39_log.csv","15");
//
//        RealClientList client = TestMain.Parse3(0,0 ,"D:\\GoogleDrive\\Roi PhD\\JammingMafat2018\\src\\Jamming\\gnss_status_2018_03_25_14_23_43_log.csv", "D:\\GoogleDrive\\Roi PhD\\JammingMafat2018\\src\\Jamming\\location_2018_03_25_14_23_43_log.csv","15");
//        int j=0;
//        for(RealClient tmp22: client.Clients)
//        {
//            //(tmp22.getUtmX()>0)
//                tmp22.ComputeUtm();
//                //if(tmp22.getLat()!=0) {
//                    System.out.println(j + "," + tmp22);
//                    j++;
//                //}
//        }
//        System.out.println("Done export");
//        List<RealClientList> clientList = new ArrayList<RealClientList>();
//        clientList.add(client);
//        RealClientList relevantClients =new RealClientList();
//        stage.setTitle("Menu Sample");
//        final GraphicsContext[] gc = new GraphicsContext[1];
//        Scene scene = new Scene(new VBox(), xSize, ySize);
//        Canvas canvas = new Canvas(xSize, ySize);
//        scene.setFill(Color.OLDLACE);
//
//        MenuBar menuBar = new MenuBar();
//
//        // --- Menu File
//        Menu menuFile = new Menu("File");
//
//        MenuItem clear = new MenuItem("Clear");
//        MenuItem exit = new MenuItem("Exit");
//        exit.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent t) {
//                System.exit(0);
//            }
//        });
//        MenuItem import_ = new MenuItem("Import CSV file");
//        import_.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent t) {
//                System.exit(0);
//            }
//        });
//        menuFile.getItems().addAll( import_,exit);
//
//        // --- Menu Edit
//        Menu menuEdit = new Menu("Scenario");
//        final VBox vbox = new VBox();
//        vbox.setAlignment(CENTER);
//        MenuItem img = new MenuItem("Load Image");
//        img.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent t) {
//
//                Image image = new Image(imagePath);
//                 gc[0] = canvas.getGraphicsContext2D();
//// Draw the Image
//                //gc.setGlobalAlpha(0.5);
//                gc[0].drawImage(image,0, 0, scene.getWidth(), scene.getHeight());
//                gc[0].setGlobalAlpha(1);
//            }
//        });
//        MenuItem particle = new MenuItem("Spread Particles");
//        particle.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent t) {
//                gc[0].setLineWidth(3);
//
//                int i=0;
//                gc[0].setStroke(Color.BLACK);
//                for( JammerParticle tmp : particle_list.getJamList())
//                {
//                    Point2D points = convertUTMtoScreenCOrdinates(tmp.getJamLoc().getX(),tmp.getJamLoc().getY());
//                    // double maxGPSsnr = tmp.Clients.get(i).getMaxSnrGPS();
//                   // double maxGPSsnr = tmp.Clients.get(i).getMaxSnrGlonass();
//                    double x =points.getX();
//                    double y = points.getY();
//                    gc[0].strokeLine(x,y,x,y);
//
//                }
//
//            }
//        });
//        MenuItem par_progress = new MenuItem("Progress Particles (and Resample)");
//        par_progress.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent t) {
//                //String imagePath = "Ariel_photo.png";
//                Image image = new Image(imagePath);
//                gc[0] = canvas.getGraphicsContext2D();
//// Draw the Image
//                //gc.setGlobalAlpha(0.5);
//                gc[0].drawImage(image,0, 0, scene.getWidth(), scene.getHeight());
//                gc[0].setGlobalAlpha(1);
//               // particle_list.moveJammers(2);
//               // particle_list.evalWeights();
//
//            }
//        });
//
//        vbox.setAlignment(CENTER);
//        MenuItem clear_canvas = new MenuItem("Clear Canvas");
//        clear_canvas.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent t) {
//
//                GraphicsContext gc = canvas.getGraphicsContext2D();
//                ClearScreen(gc);
//            }
//        });
//        menuEdit.getItems().addAll(img,clear_canvas);
//
//
//
//        // --- Menu View
//        Menu menuView = new Menu("Run");
//        MenuItem next_phase = new MenuItem("1sec Next Phase");
//        next_phase.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent t) {
//                Color[] colors = {Color.BLACK,Color.RED,Color.ORANGE, Color.YELLOW,Color.GREENYELLOW,Color.GREEN,Color.DARKGREEN,Color.BLUE};
//                Random R1 = new Random();
//               // RealClientList relevantClients = null;
//                Image image = new Image(imagePath);
//                gc[0] = canvas.getGraphicsContext2D();
//// Draw the Image
//                //gc.setGlobalAlpha(0.5);
//                gc[0].drawImage(image,0, 0, scene.getWidth(), scene.getHeight());
//                gc[0].setGlobalAlpha(1);
//
//                ArrayList<RealClient> relevantClient = new ArrayList<>();
//                gc[0].setLineWidth(8);
//                for (RealClientList clientList : clientList)
//                {
//                    RealClient tmp = clientList.getClinetTimeStampByUTC_time(current_UTC_time);
//
//                    if (tmp != null)
//                    {
//                        Point2D points = convertUTMtoScreenCOrdinates(tmp.getUtmX(), tmp.getUtmY());
//                        //double maxGPSsnr = tmp.Clients.get(i).getMaxSnrGPS();
//                        double maxGPSsnr = tmp.getMaxSnrGPS();
//                        int color = getColorbySNR(maxGPSsnr);
//                        double x = points.getX();
//                        double y = points.getY();
//                        tmp.setXY_screen_cords(x,y);
//                        relevantClient.add(tmp);
//                      //  System.out.print(x+ " "+y+ "...");
//                        System.out.println("Array size is "+ relevantClient.size()+" x value: "+x+" y value :"+y + "time "+current_UTC_time) ;
//                        gc[0].setStroke(colors[color]);
//                        gc[0].strokeLine(x, y, x, y);
//                    }
//                }//end of for
//                current_UTC_time++;
//                //particle_list.evalWeightExperimentalData(relevantClient);
//
//                gc[0].setLineWidth(4);
//                gc[0].setStroke(Color.RED);
//                for(int i=0; i<particle_list.getJamList().size();i++)
//                {
//                    Point2D tmp = particle_list.getJamList().get(i).getJamLoc();
//                    Point2D tmp_screen = convertUTMtoScreenCOrdinates(tmp.getX(), tmp.getY());
//                    double x = tmp_screen.getX();
//                    double y = tmp_screen.getY();
//                   // System.out.println("Screeen cord jammers "+x+"  "+y+" weight:"+ particle_list.getJamList().get(i).getWeight());
//                    gc[0].strokeLine(x, y, x, y);
//
//
//
//                }
//                particle_list.Resample();
//                particle_list.moveJammers(10);
//
//                System.out.println(current_UTC_time);
//
//
//            }//end of func
//
//
//        });
//        MenuItem next_phase_10 = new MenuItem("Next Fix");
//        next_phase_10.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent t) {
//                int[][] heatmap = InitHeatmap(xSize,ySize);
//
//                System.out.println("Try: "+heatmap.length+ "   "+ heatmap[0].length);
//
//                Image image = new Image(imagePath);
//                RealClient tmp;
//                int i=0;
//                gc[0] = canvas.getGraphicsContext2D();
//                gc[0].setLineWidth(4);
//                tmp = clientList.get(0).getNextFix(current_UTC_time);
//                double oldTime=current_UTC_time;
//                double oldX=0;
//                Point2D Kikar = convertUTMtoScreenCOrdinatesWithRotation(646055,3448790, 0);
//                Point2D Misgad = convertUTMtoScreenCOrdinatesWithRotation(646247,3448727, 0);
//                Point2D Misgad2 = convertUTMtoScreenCOrdinatesWithRotation(646314.2,3448852.8, -2.7052);
//                //Point2D Misgad = convertUTMtoScreenCOrdinatesWithRotation(646314.2,3448852.8, 0);
//
//
//
//                gc[0].setLineWidth(15);
//                gc[0].strokeLine(Kikar.getX(), Kikar.getY(), Kikar.getX(), Kikar.getY());
//                gc[0].setStroke(Color.GREEN);
//                gc[0].strokeLine(Misgad2.getX(), Misgad2.getY(), Misgad2.getX(), Misgad2.getY());
//                gc[0].setStroke(Color.RED);
//                gc[0].strokeLine(Misgad.getX(), Misgad.getY(), Misgad.getX(), Misgad.getY());
//
//
//
//                double x=0;
//                double y=0;
//                double oldY=0;
//                while(tmp!=null)
//                {
//                       tmp.ComputeUtm();
//                    double diff = current_UTC_time-oldTime;
//                    oldTime = current_UTC_time;
//                    current_UTC_time = tmp.getTime();
//                    Point2D points = convertUTMtoScreenCOrdinatesWithRotation(tmp.getUtmX(), tmp.getUtmY(),0);
//                    //double maxGPSsnr = tmp.Clients.get(i).getMaxSnrGPS();
//                 //   heatmap = addPoint(heatmap,tmp, points, xSize, ySize);
//                     oldX = x;
//                     oldY=y;
//                     x = points.getX();
//                     y = points.getY();
//                    tmp.setXY_screen_cords(x, y);
//                    int color = getColorbySNR(tmp.getMaxSnrGPS());
//                    i++;
//                    gc[0].setLineWidth(4);
//                    if(diff>10 && diff<=40) {
//                        gc[0].setStroke(Color.BLUE);
//                        //System.out.println("Secnods pass : " + diff/10);
//                    }
//
//                    else if(diff>40)
//                        gc[0].setStroke(Color.GREEN);
//                    else if (diff<=10)
//                        gc[0].setStroke(Color.RED);
//
//                    gc[0].strokeLine(x, y, oldX, oldY);
//                    Color[] colors = {Color.BLACK,Color.RED,Color.ORANGE, Color.YELLOW,Color.GREENYELLOW,Color.GREEN,Color.DARKGREEN,Color.BLUE};
//                    double maxGPSsnr = tmp.getMaxSnrGPS();
//
//
//                    gc[0].setStroke(colors[color]);
//
//                    gc[0].strokeLine(x, y-5, x, y-5);
//
//
//
//                     tmp = clientList.get(0).getNextFix(current_UTC_time);
//
//
//                }
//                System.out.println("FInish iterate");
//                gc[0].setLineWidth(1);
//                Color[] colors = {Color.BLACK,Color.GRAY,Color.ORANGE, Color.YELLOW,Color.GREENYELLOW,Color.GREEN,Color.DARKGREEN,Color.BLUE};
//                int number=0;
////                for(i=0;i<xSize;i++)
////                    for(int j=0;j<ySize;j++)
////                    {
////
////
////                        int col = 19*heatmap[i][j];
////                        gc[0].setStroke(Color.rgb(col,col,col));
////                        gc[0].strokeLine(i,j,i,j);
////                    }
//                System.out.println(number+ "  .......");
//            }
//
//
//
//
//        });
//
//
//
//        menuView.getItems().addAll(next_phase,next_phase_10,particle,par_progress);
//
//        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
//
//
//        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, canvas);
//
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    private double getRadiusFromSnr(double maxSnr, double absMaxSnr) {
//        double diff = absMaxSnr-maxSnr;
//        return maxSnr*20;
//    }
//
//
//    private int getColorbySNR(double maxGPSsnr) {
//        if (maxGPSsnr<10)
//            return 0;
//        else if(maxGPSsnr<17)
//            return 1;
//        else if (maxGPSsnr<22)
//            return 2;
//        else if (maxGPSsnr<35)
//            return 3;
//        else if (maxGPSsnr<30)
//            return 4;
//        else if (maxGPSsnr<35)
//            return 5;
//        else if(maxGPSsnr<40)
//            return 6;
//        return 7;
//    }
//
//    private void ClearScreen(GraphicsContext gc) {
//        Canvas tmp =  gc.getCanvas();
//        double height = tmp.getHeight();
//        double width = tmp.getWidth();
//        gc.clearRect(0, 0, width, height);
//
//
//    }
//    private class PageData {
//        public String name;
//        public String description;
//        public String binNames;
//        public Image image;
//        public PageData(String name, String description, String binNames) {
//            this.name = name;
//            this.description = description;
//            this.binNames = binNames;
//            //image = new Image(getClass().getResourceAsStream(name + ".jpg"));
//        }
//    }
//}
////public class Exp_UI_ver0 {
//
