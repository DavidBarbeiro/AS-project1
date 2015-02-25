package sample;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class generator {
	static final int timeid = 0;
	static final int vid = 1;
	static final int aid = 2;
	static final int pid = 3;
	static final int tid = 4;

	static void record(DataOutputStream f, double v, double a, double p,
			double t) throws IOException {
		f.writeInt(timeid);
		f.writeLong(System.currentTimeMillis());
		f.writeInt(vid);
		f.writeDouble(v);
		f.writeInt(aid);
		f.writeDouble(a);
		f.writeInt(pid);
		f.writeDouble(p);
		f.writeInt(tid);
		f.writeDouble(t);
	}

	public static void main(String argv[]) {

		String fileName = "NewData.dat";
		DataOutputStream file = null;
		double v, a, p, t;
		Random x = new Random(System.currentTimeMillis());

		try {
			file = new DataOutputStream(new FileOutputStream(fileName));

			// change the constants in the for loop, to generate values in the
			// ranges you want
			for (int i = 1; i < 100; i++) {
				v = 250 + 50 * x.nextDouble();
				a = 10000 + 200 * x.nextDouble();
				p = 40 + 60 * x.nextDouble();
				t = -50 + 100 * x.nextDouble();
				record(file, v, a, p, t);
			}
			file.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
