package pbo.f01.model;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "dorms")
public class Dorm {

    @Id
    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "gender", nullable = false, length = 8)
    private String gender;

    @OneToMany(mappedBy = "dorm", targetEntity = Student.class, cascade = CascadeType.ALL)
    private Set<Student> residents;

    // public Dorm() {
    //     // Konstruktor tanpa argumen
    // }

    public Dorm(String name, Integer capacity, String gender) {
        this.name = name;
        this.capacity = capacity;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getResidentNum() {
        return residents.size();
    }

    public boolean addResident(Student student) {
        if (!student.getGender().equals(this.gender) || this.getResidentNum() >= this.capacity) {
            return false;
        }

        student.setDorm(this);
        return residents.add(student);
    }

    public Set<Student> getResidents() {
        Set<Student> sortedResidents = new TreeSet<>((s1, s2) -> s1.getName().compareTo(s2.getName()));
        sortedResidents.addAll(residents);
        return sortedResidents;
    }

    public void setResidents(Set<Student> residents) {
        this.residents = residents;
    }

    public boolean hasSpace() {
        return this.capacity > this.getResidentNum();
    }

    public boolean isAvailableFor(Student student) {
        return this.hasSpace() && this.gender.equals(student.getGender());
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%d|%d", name, gender, capacity, getResidentNum());
    }
}
