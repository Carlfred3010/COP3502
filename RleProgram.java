import java.util.Scanner;

public class RleProgram {
    public static void main(String[] args){ // Main method
        Scanner scnr = new Scanner(System.in);
        //The different variables used
        String fileName;
        int option;
        String rleString;
        String hexRle;
        String hexFlat;
        byte [] imageFile;
        //Welcome message and initial menu display
        System.out.println("Welcome to the RLE image encoder! ");
        System.out.println();
        System.out.println("Displaying Spectrum Image: ");
        ConsoleGfx.displayImage(ConsoleGfx.testRainbow);
        System.out.println();
        System.out.println("RLE Menu");
        System.out.println("--------");
        System.out.println("0. Exit");
        System.out.println("1. Load File");
        System.out.println("2. Load Test Image");
        System.out.println("3. Read RLE String");
        System.out.println("4. Read RLE Hex String");
        System.out.println("5. Read Data Hex String");
        System.out.println("6. Display Image");
        System.out.println("7. Display RLE String");
        System.out.println("8. Display Hex RLE Data");
        System.out.println("9. Display Hex Flat Data");
        System.out.println();
        System.out.println("Select a Menu Option: ");
        option = scnr.nextInt();
        //imageFile initialized to null for the project to work
        imageFile = null;
        while (option != 0) {
            //The switch option displays what happens depending on the user's input.
            switch (option) {
                case 1://loads chosen file
                    System.out.println("Enter name of file to load: ");
                     fileName = scnr.next();
                    ConsoleGfx.loadFile(fileName);
                    break;
                case 2://loads the test image file
                    imageFile = ConsoleGfx.testImage;
                    System.out.println("Test image data loaded.");
                    break;
                case 3://decodes an rle string to be read with delimiters
                    System.out.println("Enter an RLE string to be decoded: ");
                    rleString = scnr.next();
                    imageFile = decodeRle(stringToRle(rleString));
                    break;
                case 4:// reads the rle string in hexadecimal notation without delimiters
                    System.out.println("Enter the Hex String holding RLE data: ");
                    hexRle = scnr.next();
                    imageFile = decodeRle(stringToData(hexRle));
                    break;
                case 5://reads raw data in hexadecimal notation
                    System.out.println("Enter the hex string holding flat data: ");
                    hexFlat = scnr.next();
                    imageFile =stringToData(hexFlat);
                    break;
                case 6://displays the current image
                    System.out.println("Displaying image...");
                    ConsoleGfx.displayImage(imageFile);
                    break;
                case 7://converts data into its human readable form
                    if(imageFile== null){
                        rleString = "(no data)";
                    }
                    else {
                        rleString = toRleString(encodeRle(imageFile));
                        System.out.println("RLE representation: " + rleString);
                    }
                    break;
                case 8://coverts data into a hexadecimal string without delimiters
                    if(imageFile== null){
                        hexRle = "(no data)";
                    }
                    else {
                        hexRle = toHexString(encodeRle(imageFile));
                        System.out.println("RLE hex values: "+ hexRle);
                    }
                    break;
                case 9://displays the raw data in hexadecimal notation
                    if(imageFile== null){
                        hexFlat = "(no data)";
                    }
                    else {
                        hexFlat = toHexString(imageFile);
                        System.out.println("Flat hex values: " + hexFlat);
                    }
                    break;
                default:
                    System.out.println("Error! Invalid input. ");

            }
            //The menu redisplays after a choice has been made.
            System.out.println("RLE Menu");
            System.out.println("--------");
            System.out.println("0. Exit");
            System.out.println("1. Load File");
            System.out.println("2. Load Test Image");
            System.out.println("3. Read RLE String");
            System.out.println("4. Read RLE Hex String");
            System.out.println("5. Read Data Hex String");
            System.out.println("6. Display Image");
            System.out.println("7. Display RLE String");
            System.out.println("8. Display Hex RLE Data");
            System.out.println("9. Display Hex Flat Data");
            System.out.println();
            System.out.println("Select a Menu Option: ");
            option = scnr.nextInt();
        }

    }

    public static String toHexString(byte[] data){//Method 1
        String hex;
        hex = "";
        // loop through data and create a hex string using char from each byte
        for (int i =0; i < data.length; i++){
            int x = data[i];
            hex += Integer.toHexString(x); // append
        }
        return hex;
    }

    // function to count the runs
    public static int countRuns(byte[] flatData) {//Method 2
        int numRuns = 1; // start at 1
        int runLength = 1;
        // loop through data and consider a run when the next byte is not equal to current byte or when run length is more than 15
        for(int i = 0; i < flatData.length - 1; i++){
            if(flatData[i] != flatData[i+1]){
                numRuns += 1;
                runLength = 1;
            }else{
                runLength += 1;
            }

            if(runLength > 15){
                numRuns += 1;
                runLength = 1;
            }
        }

        return numRuns;
    }

    // encode flat data into rle data
    public static byte[] encodeRle(byte[] flatData){//Method 3
        byte[] arr = new byte[2*countRuns(flatData)];
        int totalRunCount = countRuns(flatData);
        // return array
        byte[] finalRleString = new byte[2 * totalRunCount];
        byte countOfRepeat = 1; // the number of repeating bytes in the current run
        byte repeatingData = 0; // the data that is being repeating
        int runCount = 0; // the current run we are encoding
        int numberOfStreaks = 0; // number of 15 length runs
        // loop through flat data
        for(int i = 0; i < flatData.length-1; i++){
            repeatingData = flatData[i];
            // test if equal then add to count
            if(repeatingData == flatData[i+1]){
                countOfRepeat += 1;
            }
            // if count is more than 15 or if next data is different than current, break the run and add to rle data
            if(countOfRepeat >= 15 || repeatingData != flatData[i+1]) {

                if(countOfRepeat >= 15){
                    numberOfStreaks += 1;
                }


                // set the data on rle data
                finalRleString[(runCount*2)] = (byte)(countOfRepeat == 15 ? countOfRepeat : countOfRepeat - numberOfStreaks);
                finalRleString[(runCount*2)+1] = repeatingData;
                // reset the counters
                countOfRepeat = 1;
                runCount += 1;

                if (repeatingData != flatData[i+1]) {
                    numberOfStreaks = 0;
                }
            }
        }

        // count the final set of data
        finalRleString[(totalRunCount-1)*2] = countOfRepeat;
        finalRleString[((totalRunCount-1)*2)+1] = flatData[flatData.length-1];

        return finalRleString;
    }

    public static int getDecodedLength(byte[] rleData){//Method 4
        int length = 0;//keeps track of length
        // add up every other number (which tracks the length of each run)
        for (int i = 0; i < rleData.length; i++){
            if (i % 2==0)
                length+= rleData[i];
        }
        return length;
    }

    public static byte[] decodeRle(byte[] rleData){//Method 5
        byte[] arr = new byte[getDecodedLength(rleData)];
        int x = 0;
        // loop through every other number in the rle data
        for(int i =0; i < rleData.length; i+=2){
            // loop through the number of times
            for (int j = 0; j < rleData[i]; j++){
                // rleData[i+1] holds the data to copy, set the data to the array
                arr[x] = rleData[i+1];
                if (x==arr.length-1)
                    break;
                x++;
            }
        }
        return arr;
    }

    //
    public static byte[] stringToData(String dataString){//Method 6
        String num;
        int data;
        byte[] arr = new byte[dataString.length()];//The array will hold the value of the converted String
        int x = 0;
        for (int i =0; i < dataString.length(); i++){
            num = ""+ dataString.charAt(i);
            data = Integer.parseInt(num, 16);//Converts the values to hexadecimal
            arr[x] = (byte) data;
            x++;
        }
        return arr;
    }
    public static String toRleString(byte[] rleData){//Method 7
        String rleString;
        rleString = "";
        for (int i =0; i < rleData.length; i++){
            if (i % 2 ==0){//Separates the different runs
                rleString+= String.valueOf(rleData[i]);
            }
            else{
                rleString+= Integer.toHexString(rleData[i]);
                rleString+= ":";//Adds colon delimiter between the different runs
            }
        }
        rleString = rleString.substring(0, rleString.length()-1);
        return rleString;
    }

    public static byte[] stringToRle(String rleString){//Method 8
        //Reverse of method 7
        String hexString;
        hexString = "";
        char x1;
        char x2;
        String y;
        int a;
        String [] arr;
        arr = rleString.split("\\:");
        byte[] rle;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length() % 2 == 0) {
                x1 = arr[i].charAt(0);
                x2 = arr[i].charAt(1);
                hexString += x1;
                hexString += x2;
            } else {
                y = "" + arr[i].charAt(0) + arr[i].charAt(1);
                a = Integer.parseInt(y);
                x1 = arr[i].charAt(2);
                hexString += Integer.toHexString(a);
                hexString += x1;
            }
        }
        rle = stringToData(hexString);
        return rle;
    }


}
