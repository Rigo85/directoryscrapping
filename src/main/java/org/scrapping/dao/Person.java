package org.scrapping.dao;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>
 * <p>
 * Copyright 2016 by Rigoberto Leander Salgado Reyes.
 * <p>
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.
 */
@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "idInsignia", nullable = false)
    private String idInsignia;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "noExp", nullable = false)
    private String noExp;

    @Column(name = "province", nullable = false)
    private String province;

    @Column(name = "town", nullable = false)
    private String town;

    @Column(name = "flat", nullable = false)
    private String flat;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "area", nullable = false)
    private String area;

    @Column(name = "faculty", nullable = false)
    private String faculty;

    @Column(name = "facultyGroup", nullable = false)
    private String facultyGroup;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "gender", nullable = false)
    private char gender;

    public Person() {
    }

    public Person(String firstName, String lastName, String userName, String idInsignia, String category,
                  String noExp, String province, String town, String flat, String phone, String area,
                  String faculty, String facultyGroup, String position, char gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.idInsignia = idInsignia;
        this.category = category;
        this.noExp = noExp;
        this.province = province;
        this.town = town;
        this.flat = flat;
        this.phone = phone;
        this.area = area;
        this.faculty = faculty;
        this.facultyGroup = facultyGroup;
        this.position = position;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdInsignia() {
        return idInsignia;
    }

    public void setIdInsignia(String idInsignia) {
        this.idInsignia = idInsignia;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNoExp() {
        return noExp;
    }

    public void setNoExp(String noExp) {
        this.noExp = noExp;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getFacultyGroup() {
        return facultyGroup;
    }

    public void setFacultyGroup(String facultyGroup) {
        this.facultyGroup = facultyGroup;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        return gender == person.gender &&
                id.equals(person.id) &&
                firstName.equals(person.firstName) &&
                lastName.equals(person.lastName) &&
                userName.equals(person.userName) &&
                idInsignia.equals(person.idInsignia) &&
                category.equals(person.category) &&
                noExp.equals(person.noExp) &&
                province.equals(person.province) &&
                town.equals(person.town) &&
                flat.equals(person.flat) &&
                phone.equals(person.phone) &&
                area.equals(person.area) &&
                faculty.equals(person.faculty) &&
                facultyGroup.equals(person.facultyGroup) &&
                position.equals(person.position);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + idInsignia.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + noExp.hashCode();
        result = 31 * result + province.hashCode();
        result = 31 * result + town.hashCode();
        result = 31 * result + flat.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + area.hashCode();
        result = 31 * result + faculty.hashCode();
        result = 31 * result + facultyGroup.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + (int) gender;
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", idInsignia='" + idInsignia + '\'' +
                ", category='" + category + '\'' +
                ", noExp='" + noExp + '\'' +
                ", province='" + province + '\'' +
                ", town='" + town + '\'' +
                ", flat='" + flat + '\'' +
                ", phone='" + phone + '\'' +
                ", area='" + area + '\'' +
                ", faculty='" + faculty + '\'' +
                ", facultyGroup='" + facultyGroup + '\'' +
                ", position='" + position + '\'' +
                ", gender=" + gender +
                '}';
    }
}
