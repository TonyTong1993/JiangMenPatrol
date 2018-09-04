package com.ecity.cswatersupply.contact;

import com.ecity.cswatersupply.contact.model.Contact;
import com.ecity.cswatersupply.contact.model.ContactGroup;

import java.util.ArrayList;
import java.util.List;

public class GenerateDataSource {

	public static ContactGroup generateDatas() {
		ContactGroup contactGroup = new ContactGroup();
		contactGroup.setGroupName("武汉众智鸿图技术有限公司");

		List<ContactGroup> firstChild = new ArrayList<ContactGroup>();
		ContactGroup contactGroup1 = new ContactGroup();
		contactGroup1.setGroupName("运营管理中心");
		firstChild.add(contactGroup1);

		List<ContactGroup> secondChild = new ArrayList<ContactGroup>();
		ContactGroup contactGroup2 = new ContactGroup();
		contactGroup2.setGroupName("财务");

		List<Contact> contacts = new ArrayList<Contact>();
		Contact contact1 = new Contact("运营管理中心","售前技术支持","张金娣","女","财务经理","13465776688","021578234");
		contacts.add(contact1);
		Contact contact2 = new Contact("运营管理中心","售前技术支持","刘晗","女","财务专员","13465760698","021578234");
		contacts.add(contact2);
		Contact contact3 = new Contact("运营管理中心","售前技术支持","敏佳","女","财务专员","13465700008","021578234");
		contacts.add(contact3);
		contactGroup2.setContacts(contacts);
		secondChild.add(contactGroup2);

		ContactGroup contactGroup3 = new ContactGroup();
		contactGroup3.setGroupName("人力资源");

		List<Contact> contacts1 = new ArrayList<Contact>();
		Contact contact4 = new Contact("运营管理中心","人力资源","李丹丹","女","人事主管","13465776688","021578234");
		contacts1.add(contact4);
		Contact contact5 = new Contact("运营管理中心","人力资源","陶晶","女","人事专员","13465760698","021578234");
		contacts1.add(contact5);
		contactGroup3.setContacts(contacts1);
		secondChild.add(contactGroup3);

		contactGroup1.setChildGroup(secondChild);

		contactGroup.setChildGroup(firstChild);

		return contactGroup;
	}

}
