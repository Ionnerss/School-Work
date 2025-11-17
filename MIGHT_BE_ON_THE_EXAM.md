LIST OF STUFF THAT MIGHT BE ON THE EXAM SINCE HE LOVES GIVING NICHE QUESTIONS:

1. int[] scores  == int scores [] == int scores[]

2. int[] scores = {1,2,3}
system.out.println("Scores: " + scores);
this will print out the adress of scores so some random bs. you have to add [] to get specific values.

3. 
  public class MyClass {

    public static void main {

        String num = "999999"

        int sum = 0;

        for (int i; i < num.length(); i++) {
          sum += num.charAt(i) - '0';
          SOP("SUM" + sum);
        }
    }
  }

  We do - '0' since charAt gives the ascii value of the character, therefore we take the ascii value of num before the one needed
  to cancel acii value out and get the true value.

4. 
  public class MyClass {

    int ii;

    public static void main {

      MyClass m1 = new MyClass();
      MyClass m2 = new MyClass();

      SOP(m1);

      m2.ii = 5;

      SOP(m1);
      SOP(m2);
    }
  }

  OUTPUT:
  0
  5
  5

  Assigning a value to ii in any instance of MyClass (m1, m2, ...) then it will change the value for every instance.

5. this.whatever this -> represents the current instance of an object.

6. Alias -> multiple keys to a piece of data (memory location);

7. 
  MyClass m1 = new MyClass();
  MyClass m2 = new MyClass();
  m1 = 5;
  m2 = 5;

  SOP(m1 == m2); //Output: false since its 2 different memory slots
  SOP(m1.equals(anotherClass.m2)); //Output since its 





---------------------------------------------------------------------------------
IMPORTANT NOTES FOR REFERENCE:
  - When writing loops or functions state the fail condition first so we can assume the remaining code 
  is perfect. This shortens the amount of processes the computer has to go through and reduces the amount of
  data used.
