import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


public class TwoWaySerialComm
{
	public static String newline = System.getProperty("line.separator");
    static String RGB = "255 0 0 256";
	static int rgbSet = 1;
	SerialPort serialPort;

    public TwoWaySerialComm()
    {
        super();
    }
    
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                Thread.sleep(2000); //required for arduino listen
                OutputStream out = serialPort.getOutputStream();
                
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    public String getRGB() {
		return RGB;
	}

	public void setRGB(String rGB) throws InterruptedException, IOException {
		RGB = rGB;
		System.out.println("SETRGB: " + RGB);
		setRgbSet(1);

		Thread.sleep(200); //required for arduino listen
        OutputStream out = serialPort.getOutputStream();
        
        (new Thread(new SerialWriter(out))).start();
	}
	
	 public static int getRgbSet() {
			return rgbSet;
		}
	
		public void setRgbSet(int rgbset) {
			rgbSet = rgbset;
		}

	/** */
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                    System.out.print(new String(buffer,0,len));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    
    /**
	 * Writes a new line in the printer
	 */
	public static void newLine(OutputStream outputStream) throws IOException {
		outputStream.write(new byte[] { 10, 13 });
	}

    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {                
                int c = 0;              
//                while ( ( c = rgbSet) > -1 )
//                {
                    //this.out.write(c);            
//                	byte out[] = new byte[3];
//                	out[0] = (byte) Integer.parseInt(RGBA[0]);
//                	out[1] = (byte) Integer.parseInt(RGBA[1]);
//                	out[2] = (byte) Integer.parseInt(RGBA[2]);          	
//                    this.out.write(Integer.parseInt(RGBA[0]));
//                    this.out.write(Integer.parseInt(RGBA[1]));
//                    this.out.write(Integer.parseInt(RGBA[2]));
//                    this.out.write(Integer.parseInt(RGBA[3]));
                	//this.out.write(RGB.getBytes(), 0, RGB.length());
                	//System.out.println(rgbSet);
                	if (rgbSet == 1) {  
                    	String[] RGBA = RGB.split(" ");
                    	System.out.println("INSIDEIF: " + RGB);
                	try {
                	    byte[] bytes = RGB.getBytes();
//                	    System.out.print(bytes);
                	    for (int i=0; i<bytes.length; i++) 
                	        this.out.write(bytes[i]);    
                	    this.out.flush();
                	}
                	catch (UnsupportedEncodingException e) {}
                    rgbSet--;
                	} else {
                		rgbSet=0;
                	}
                
//                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    
    public static void main ( String[] args )
    {
        try
        {
            (new TwoWaySerialComm()).connect("COM4");
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}