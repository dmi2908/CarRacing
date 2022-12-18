import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CarRacing {
    static int width = 800, height = 600;
    static JFrame frame = new JFrame();
    static int min = 1;
    static int max = 5;
    static RunCars[] runCars = new RunCars[5];
    static int finish = 670;

    public static class RunCars extends Thread {
        private int sleep;
        CreateCar car;
        public RunCars(CreateCar car,int sleep) {
            this.car = car;
            this.sleep = sleep;
        }
        @Override
        public void run() {
            for (int t = 0; t < 360;) {
                try {
                    Thread.sleep(sleep);
                    max -= min;
                    car.dX += (int) (Math.random() * ++max) + min;
                    car.l.setBounds(car.dX, car.dY, car.w, car.h);
                    if (car.dX >= finish) {
                        runCars[0].interrupt();
                        runCars[1].interrupt();
                        runCars[2].interrupt();
                        runCars[3].interrupt();
                        runCars[4].interrupt();
                        JOptionPane.showMessageDialog(frame, "Победила машина номер - " + car.carNum);
                        break;
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                t = t == 359 ? 0 : t + 1;
            }
        }
    }

    public static class CreateCar{
        private String path;
        public int w, h;
        public int dX,dY;
        public JLabel l;
        public int carNum;
        public CreateCar(String path, int x, int y, int car) throws IOException {
            this.path = path;
            BufferedImage im = ImageIO.read(new File("./src/cars/"+path));
            w = im.getWidth();
            h = im.getHeight();
            carNum = car;
            l = new JLabel(new ImageIcon(im));
            l.setBounds(x, y, w, h);
            dY = y;
            frame.add(l);
        }
    }

    public static void main(String[] args) throws IOException {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Гонки!!!");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(dim.width / 2 - width / 2, dim.height / 2 - height / 2, width, height + 30);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.black);
        frame.setVisible(true);
        String[][] cars = new String[][]{new String []{"car1","0","0","55"}, new String []{"car2","0","100", "15"},{"car3","0","200", "25"},{"car4","0","300", "35"},{"car5","0","400","45"}};
        for (int i = 0; i < cars.length; i++) {
            RunCars runCar = new RunCars(new CreateCar(cars[i][0] + ".png", Integer.valueOf(cars[i][1]), Integer.valueOf(cars[i][2]), i), Integer.valueOf(cars[i][3]));
            runCar.start();
            runCars[i] = runCar;
        }
    }
}