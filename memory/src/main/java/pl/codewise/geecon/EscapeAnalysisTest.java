package pl.codewise.geecon;

public class EscapeAnalysisTest {

    public static void main(String[] args) {
        Employee employee = new Employee("UNIT", new Person("The Doctor", 907));

        while (true) {
            Person person = employee.getPerson();
            // this caller does not modify the object, so defensive copy was unnecessary
            System.out.println("Employee's name: " + person.getName() + "; age: " + person.getAge());
        }
    }
}

class Person {

    private String name;
    private int age;

    public Person(String personName, int personAge) {
        name = personName;
        age = personAge;
    }

    public Person(Person p) {
        this(p.getName(), p.getAge());
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

class Employee {

    private final String organisation;
    private final Person person;

    Employee(String organisation, Person person) {
        this.organisation = organisation;
        this.person = person;
    }

    public String getOrganisation() {
        return organisation;
    }

    // makes a defensive copy to protect against modifications by caller
    public Person getPerson() {
        return new Person(person);
    }
}
