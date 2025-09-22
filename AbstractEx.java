import java.util.Scanner;
abstract class Shape{
int dim1,dim2;
Shape (int dim1,int dim2){
this.dim1=dim1;
this.dim2=dim2;
}
abstract void printArea();
}

class Rectangle extends Shape{
Rectangle (int dim1,int dim2){
super(dim1,dim2);
}


void printArea(){
System.out.println("Area of the rectangle is "+(this.dim1*this.dim2));
}
}

class Triangle extends Shape{
Triangle (int dim1,int dim2){
super(dim1,dim2);
}

void printArea(){
System.out.println("Area of the triangle is "+(0.5*this.dim1*this.dim2));
}
}

class Circle extends Shape{
Circle (int dim1, int dim2){
super(dim1,0);
}

void printArea(){
System.out.println("Area of the Circle is "+(3.14*dim1*dim1));
}
}

public class AbstractEx{
public static void main(String[] args){
Scanner scan=new Scanner(System.in);
int choice;
while(true){
int a,b;
System.out.println("\n---Area Finder---");
System.out.println("1.Area of Rectangle \n2.Area of Triangle \n3.Area of Circle \n4.Exit");
System.out.print("Enter your Choice:");
choice=scan.nextInt();
switch(choice){
case 1: {
System.out.print("Enter the length:");
a=scan.nextInt();
System.out.print("Enter the breath:");
b=scan.nextInt();
Shape rect=new Rectangle(a,b);
rect.printArea();
break;
}
case 2: {
System.out.print("Enter the height:");
a=scan.nextInt();
System.out.print("Enter the breath:");
b=scan.nextInt();
Shape tri=new Triangle(a,b);
tri.printArea();
break;
}
case 3: {
System.out.print("Enter the radius:");
a=scan.nextInt();
Shape cir=new Circle(a,0);
cir.printArea();
break;
}
case 4:{
System.out.println("Exiting...");
return;
}
default:{
System.out.println("Invalid Choice!, Try Again!");
}
}
}
}
}
