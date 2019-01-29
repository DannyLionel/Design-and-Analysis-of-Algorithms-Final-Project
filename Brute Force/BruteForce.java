package BruteForce;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
// This is the code to to brute force a Vigenere Cipher.
public class BruteForce
{
// Initialize some variables needed.
public static ArrayList<String> dictionary = new ArrayList <String>();
public static String plain_text, encrypted_text = "";
public static int key_length;
public static boolean key_found = false;
// First we generate plain text from a text file.
public static void generate_plain_text(String filename, int size) throws IOException {
System.out.println("Reading file " + filename + " and generating plaintext of size " +
size + ".\n");
FileReader filereader = new FileReader(filename);
BufferedReader bufferedreader = new BufferedReader(filereader);
StringBuilder stringbuilder = new StringBuilder();
String line = bufferedreader.readLine();
// Add all the contents in the file and put it in a String.

while (line != null) {
stringbuilder.append(line);
line = bufferedreader.readLine();
}
plain_text = stringbuilder.toString();
plain_text = plain_text.replaceAll("[^a-zA-Z]", "");
plain_text = plain_text.toLowerCase();
plain_text = plain_text.substring(0, size);
bufferedreader.close();
System.out.println("This is the plain text:\n\n"+ plain_text + "\n");
// Filter out everything except characters and store it as plain text.
}
public static void populate_dictionary (String filename) throws FileNotFoundException {
// A dictionary is imported in order to check if the decrypted text is in fact
// in English Language.
System.out.println("Populating the dictionary ...\n");
File f = new File (filename);
Scanner s = new Scanner (f);
while (s.hasNextLine()) {
dictionary.add(s.nextLine());
}
System.out.println("Populated the dictionary.\n");
}
// After the text is encrypted, the plain text needs to be decrypted. First step
// is to find the length of the key used.
public static void find_key_length (String text) {
System.out.println("Cracking the code .....");
System.out.println("Figuring out the key length .....\n");
ArrayList <Integer> spacings = new ArrayList<Integer> ();
ArrayList <String> substring_text = new ArrayList<String> ();
// A for loop is used to iterate through substrings of the encrypted text.
// Starting with sizes 2 to 9 (taken as average length of words in English Language),
// each substring is checked to see if there is any repetitions. If repetitions are found,
// then mark down the spaces between repetitions.
for (int sub_string_length = 2; sub_string_length < 9; sub_string_length ++) {
for (int i = 0; i < text.length()- sub_string_length -1 ; i++) {
String sub1 = text.substring(i, i+ sub_string_length);
int start, finish;
start = i;
for (int j = i; j < text.length()- sub_string_length -1; j++) {
String sub2 = text.substring(j+1 , j+ sub_string_length +1);
if (sub1.equalsIgnoreCase(sub2)) {
substring_text.add(sub1);
finish = j+1;
spacings.add(finish - start);
break;
}
}
}
}
// For easier view a grid is drawn.
String grid [][] = new String [substring_text.size()][7];
for (String[] row: grid) {
Arrays.fill(row, "-");}
// The spacings between repetitions are checked to see whether or not they are the
// factors of the key size.
for (int i = 0 ; i < substring_text.size(); i++) {
grid [i][0] = substring_text.get(i);
grid [i][1] = Integer.toString(spacings.get(i));
if (spacings.get(i)%3 == 0) {
grid[i][2] = "X";
}
if (spacings.get(i)%4 == 0) {
grid[i][3] = "X";
}
if (spacings.get(i)%5 == 0) {
grid[i][4] = "X";
}
if (spacings.get(i)%7 == 0) {
grid[i][5] = "X";
}
if (spacings.get(i)%11 == 0) {
grid[i][6] = "X";
}

}

System.out.println("--------------------------------------------------------------------------
----");
System.out.println(" SubString Spacing 3 4 5 7 11");
System.out.println("--------------------------------------------------------------------------
----");
for (int i = 0 ; i < substring_text.size() ; i++) {
for (int j = 0 ; j < 7 ; j++) {
System.out.printf("%10s",grid[i][j]);
System.out.print(" ");
}
System.out.println();
}
System.out.println();
// Count number of X's in each column to find the greatest. The column with the
// greatest number of X's found is the size of the key.
int count_3 = 0, count_4 = 0, count_5 = 0, count_7 = 0, count_11 = 0;
for (int i = 0; i < substring_text.size(); i++ ) {
if (grid[i][2].equals("X")) {
count_3++;
}
if (grid[i][3].equals("X")) {
count_4++;
}
if (grid[i][4].equals("X")) {
count_5++;
}
if (grid[i][5].equals("X")) {
count_7++;
}
if (grid[i][6].equals("X")) {
count_11++;
}
}
// Checking which count is the greatest and placing the key length into a variable for
// brute forcing.
HashMap<Integer, Integer> count_list = new HashMap<>();
count_list.put(3,count_3);
count_list.put(4,count_4);
count_list.put(5,count_5);
count_list.put(7,count_7);
count_list.put(11,count_11);
for (Entry<Integer, Integer> entry : count_list.entrySet()) { // Itrate through
hashmap

if (entry.getValue()==(Collections.max(count_list.values()))) {
key_length = entry.getKey();
}}
System.out.println("Possible Key Length of the encrypted text is " + key_length +
".\n");
}
// In this method brute_force is used. Using permutations of the english
// alphabets, every single possible key is checked and matched against a dictionary
// to check whether the decrypted text is the actual key.

public static void brute_force (int key_size, String txt) {
char[] alphabets = {'a','b','c','d','e','f','g','h','i','j','k','l','m',
'n','o','p','q','r','s','t','u','v','w','x','y','z'};

if (key_size == 0 && key_found == false) {
dictionary_check(txt , plain_text);
return;
}
// Recursion is used to call the brute force function again in order to crack the key.
for (int i = 0; i < alphabets.length; ++i)
{
if (key_found == false) {
String new_txt = txt + alphabets[i];
brute_force(key_size - 1, new_txt);
}
else {
break;
}
}
}
// This method decrypts the message given the encrypted text and a key.
// In order to decrypt a text, the value of an encrypted text letter at
// index x is subtracted with the value of the key at index x and modded with 26
// in order to get the plain text back.
public static String decrypt (String text, String key) {
String decrypted = "";
for (int i = 0, j = 0; i < text.length(); i++)
{
decrypted = decrypted + (char) ((text.charAt(i) - key.charAt(j) + 26) % 26 +

97);

j = ++j % key.length();
}
return decrypted;
}
// This method encrypts the message given the plain text and a key.
// In order to encrypt a text, the value of an plain text letter at
// index x is added with the value of the key at index x and modded with 26
// in order to get the encrypted text.
public static void encrypt (String text, String key) {
System.out.println("Enrcypting the plaintext. \n");
for (int i = 0, j = 0; i < text.length(); i++)
{
encrypted_text = encrypted_text + (char) (((text.charAt(i) - 97 ) +

(key.charAt(j)) - 97) % 26 + 97);
j = ++j % key.length();
}
System.out.println("This is the encrypted plain text: \n\n" + encrypted_text + "\n") ;
}
// Check to see if any word is found in the dictionary. If a word is contained in the
dictionary,
// it is most likely to be the correct key used in the decryption.
public static void dictionary_check (String key, String txt) {
String decrypted = decrypt(encrypted_text, key);
if (decrypted.equals(txt) //|| dictionary.contains(decrypted)

){
System.out.println("The key has been found: " + key + "\n");
System.out.println("Decrypted succesfully!\n \n" + decrypted + "\n");
key_found = true;
}
// A method to convert time taken into hours, mins, seconds, ms.

}
public static void time_taken(long start , long finish){
long total = finish - start;
long days = total / 86400000;
total = total % 86400000;
long hours = total / 3600000;
total = total % 3600000;
long mins = total / 60000;
total = total % 60000;
long sec = total / 1000;
total = total % 1000;
long ms = total;
System.out.printf(
" Total time taken to run the program is %d days, %d hours, %d minutes, %d

seconds, %d ms. %n",

days, hours, mins, sec, ms);

}

public static void main(String[] args) throws IOException
{
String file_name = "random.txt";
int character_size = 500;
String key_to_encrypt = "alibaba";
String dictionary_file_name = "dictionary.txt";
long start, finish;
generate_plain_text(file_name,character_size);
populate_dictionary(dictionary_file_name);
encrypt(plain_text,key_to_encrypt);
find_key_length(encrypted_text);
System.out.println("Using Brute Force to decrypt ..... \n");
start = System.currentTimeMillis();
brute_force(key_length,"");
finish = System.currentTimeMillis();
time_taken(start,finish);
}
}
