package com.ecity.android.contactmanchooser.model;

import java.io.Serializable;

import com.yokeyword.indexablelistview.IndexEntity;

/**
 * 联系人
 * 
 * @author gaokai
 *
 */
public class ContactMan extends IndexEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private int userid;
    private String department;
    private int departmentid;
    private boolean isleader;
    private boolean isonline;
    private String phone;
    private ContactManPosition position;
    private int workorderamount;
    private boolean hasChoosed;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(int departmentid) {
        this.departmentid = departmentid;
    }

    public boolean isLeader() {
        return isleader;
    }

    public void setLeader(boolean isleader) {
        this.isleader = isleader;
    }

    public boolean isIsonline() {
        return isonline;
    }

    public void setIsonline(boolean isonline) {
        this.isonline = isonline;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ContactManPosition getPosition() {
        return position;
    }

    public void setPosition(ContactManPosition position) {
        this.position = position;
    }

    public int getWorkorderamount() {
        return workorderamount;
    }

    public void setWorkorderamount(int workorderamount) {
        this.workorderamount = workorderamount;
    }

    public class ContactManPosition implements Serializable {
        private static final long serialVersionUID = 1L;
        public double x;
        public double y;
    }

    public boolean isHasChoosed() {
        return hasChoosed;
    }

    public void setHasChoosed(boolean hasChoosed) {
        this.hasChoosed = hasChoosed;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) // 先检查是否其自反性，后比较other是否为空。这样效率高
            return true;
        if (other == null)
            return false;
        if (!(other instanceof ContactMan))
            return false;

        final ContactMan otherContactMan = (ContactMan) other;
        String thisName = getName();
        String otherName = otherContactMan.getName();
        if (!thisName.equalsIgnoreCase(otherName)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + getName().hashCode();

        return result;
    }
}
