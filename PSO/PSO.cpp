#include <iostream>
#include <fstream>
#include <cctype>
#include <String>
#include <sstream>
#include <vector>
#include <algorithm>
#include <limits>
#include <list>
#include "VignereCipherEncrypt.h"
using namespace std;

string Decrypt(string text, vector <string> NewKey)
{
	for (int i = 0, j = 0; i < text.length(); i++)
	{
		decryptedMsg[i] = (((encryptedMsg[i] - NewKey[i]) + 26) % 26) + 'A';

		decryptedMsg[i] = '\0';
	}

}
string Encrypt(string Original_Message, string key)
{
	string All_Char_Key_Space = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Key Space
	int All_Char_Key_Space_length = All_Char_Key_Space.length();

	//convert string to uppercase
	transform(Original_Message.begin(), Original_Message.end(), Original_Message.begin(), ::toupper);

	//remove all everything that is not letters
	Original_Message.erase(remove_if(Original_Message.begin(), Original_Message.end(),
		[](char c) { return (!isalpha(c) && c != '\n' && c != '\r'); }), Original_Message.end());

	//convert key to uppercase
	transform(key.begin(), key.end(), key.begin(), ::toupper);

	cout << Original_Message << endl;
	cout << key << endl;
	int index = 0;
	string Encrypt_Message = "";

	//iterate over original message by each character and encrypt and add to new string
	for (int i = 0; i < Original_Message.length(); i++)
	{
		char letter = Original_Message[i];
		if (isalpha(letter))
		{
			int position = (All_Char_Key_Space.find(letter) + All_Char_Key_Space.find(key[index])) % 26;
			char Encrypt_Letter = All_Char_Key_Space[position];
			Encrypt_Message += Encrypt_Letter;
			index++;
			if (index >= key.length())
			{
				index %= key.length();
			}
			else
			{
				Encrypt_Message += letter;
				index = 0;
			}
		}

	}
	cout << Encrypt_Message << endl;
	return Encrypt_Message;
}
void PSO(int NumParticles, int KeyLength, int InputSize)
{
	double C1 = 2.05;
	double C2 = 2.05;
	double Inertia_Weight = 0.9;
	double VMin = 0.0;
	double VMax = 1.0;
	double r1 = 0.0;
	double r2 = 0.0;
	double GlobalBestKeyValue = numeric_limits<double>::infinity();
	string GlobalBestKey = "";
	int gBest = 0;
	int gBestTest = 0;
	int epoch = 0;
	bool done = false;	auto s = to_string(KeyLength);
	double RandomNum = ((double)rand() / (RAND_MAX)) + 1;//value between 0-1
	int num = rand() % 26;
	vector<string>LocalBestKey;
	vector<string>CurrentKey;
	vector<double>PFit;//pKey
	vector<double>LocalBestFit;
	vector<double>KeyVelocity;
	vector<double>FitVelocity;

	for (int i = 0; i < NumParticles; i++)//initalize particles
	{
		string NewKey = "";
		for (int j = 0; j < KeyLength; j++)
		{
			NewKey += static_cast<char>('A' + num);
		}
		LocalBestKey.push_back("");
		CurrentKey.push_back(NewKey); //initalize key
		PFit.push_back(0.0);
		LocalBestFit.push_back(std::numeric_limits<double>::infinity());//initalize fitness
		KeyVelocity.push_back(VMin + (VMax - VMin)*RandomNum);
		FitVelocity.push_back(VMin + (VMax - VMin)*RandomNum);
	}
	string text = "";
	int iteration = 100;
	while (iteration != 1 || GlobalBestKeyValue < 0.05)
	{
		for (int i = 0; i < NumParticles; i++)
		{
			string DecryptFile = Decrypt(text, CurrentKey); //decrypt with current key
		}
	}
	intialize();
}
void initialize()
{
	int total;

	for (int i = 0; i <= MAX_PARTICLES - 1; i++)
	{
		total = 0;
		for (int j = 0; j <= MAX_INPUTS - 1; j++)
		{
			particles[i].setData(j, getRandomNumber(START_RANGE_MIN, START_RANGE_MAX));
			total += particles[i].getData(j);
		} // j
		particles[i].setpBest(total);
	} // i

	return;
}
void getVelocity(int gBestIndex)
{
	/* from Kennedy & Eberhart(1995).
		vx[][] = vx[][] + 2 * rand() * (pbestx[][] - presentx[][]) +
						  2 * rand() * (pbestx[][gbest] - presentx[][])
	*/
	int testResults, bestResults;
	float vValue;

	bestResults = testProblem(gBestIndex);

	for (int i = 0; i <= MAX_PARTICLES - 1; i++)
	{
		testResults = testProblem(i);
		vValue = particles[i].getVelocity() +
			2 * gRand() * (particles[i].getpBest() - testResults) + 2 * gRand() *
			(bestResults - testResults);

		if (vValue > V_MAX) {
			particles[i].setVelocity(V_MAX);
		}
		else if (vValue < -V_MAX) {
			particles[i].setVelocity(-V_MAX);
		}
		else {
			particles[i].setVelocity(vValue);
		}
	} // i
}

void updateParticles(int gBestIndex)
{
	int total, tempData;

	for (int i = 0; i <= MAX_PARTICLES - 1; i++)
	{
		for (int j = 0; j <= MAX_INPUTS - 1; j++)
		{
			if (particles[i].getData(j) != particles[gBestIndex].getData(j))
			{
				tempData = particles[i].getData(j);
				particles[i].setData(j, tempData + static_cast<int>(particles[i].getVelocity()));
			}
		} // j

		//Check pBest value.
		total = testProblem(i);
		if (abs(TARGET - total) < particles[i].getpBest())
		{
			particles[i].setpBest(total);
		}

	} // i

}

int testProblem(int index)
{
	int total = 0;

	for (int i = 0; i <= MAX_INPUTS - 1; i++)
	{
		total += particles[index].getData(i);
	} // i

	return total;
}

float gRand()
{
	// Returns a pseudo-random float between 0.0 and 1.0
	return float(rand() / (RAND_MAX + 1.0));
}

int getRandomNumber(int low, int high)
{
	// Returns a pseudo-random integer between low and high.
	return low + int(((high - low) + 1) * rand() / (RAND_MAX + 1.0));
}

int minimum()
{
	//Returns an array index.
	int winner = 0;
	bool foundNewWinner;
	bool done = false;

	do
	{
		foundNewWinner = false;
		for (int i = 0; i <= MAX_PARTICLES - 1; i++)
		{
			if (i != winner) {             //Avoid self-comparison.
				//The minimum has to be in relation to the Target.
				if (abs(TARGET - testProblem(i)) < abs(TARGET - testProblem(winner)))
				{
					winner = i;
					foundNewWinner = true;
				}
			}
		} // i

		if (foundNewWinner == false)
		{
			done = true;
		}

	} while (!done);

	return winner;
}
void Encrypt_File(string key)
{
	ifstream input("Input.txt");
	ofstream output;

	string data;

	try
	{
		stringstream ss;
		ss << input.rdbuf();
		data = ss.str();
		cout << "File Read Successfully" << endl;
	}

	catch (exception e)
	{
		cout << e.what();
	}
	input.close();
	string Encrypted_Data = Encrypt(data, key);
	cout << "File Data Encrypted Successfully" << endl;
	try
	{
		output.open("Output.txt");
		output << Encrypted_Data;
		output.close();
		cout << "Encrypted Data stored on File" << endl;
	}
	catch (exception ee)
	{
		cout << ee.what();
	}


}
int main()
{
	//read a file char by char
	cout << "Program for Vignere Cipher" << endl;
	string key = "OMGI";
	Encrypt_File(key);
//change the input size
	PSO(100,4,100);//number of particles, key length, and inputsize
	system("pause");
	return 0;

}
