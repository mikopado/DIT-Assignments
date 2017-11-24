using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CA1_SendText.Models
{
    public class MockDatabase : IDataAccess<Contact>
    {
        private ICollection<Contact> contacts;

        public MockDatabase()
        {
            contacts = new List<Contact>()
            {
                new Contact(1, "Jack", "Sparrow", new PhoneNo { Prefix = "089", PhoneNumber = "5789987" }),
                new Contact(2, "Carl", "Cox", new PhoneNo { Prefix = "085", PhoneNumber = "4578345" }),
                new Contact(3, "Robert", "Mallow", new PhoneNo { Prefix = "086", PhoneNumber = "9987023" }),
                new Contact(4, "Richard", "Bell", new PhoneNo { Prefix = "085", PhoneNumber = "7643129" }),
                new Contact(5, "Liam", "Norman", new PhoneNo { Prefix = "089", PhoneNumber = "8967453" }),
                new Contact(6, "Niall", "Bergen", new PhoneNo { Prefix = "086", PhoneNumber = "6754912" }),
                new Contact(7, "Mark", "Carlson", new PhoneNo { Prefix = "088", PhoneNumber = "7843774" }),
                new Contact(8, "Rose", "Martin", new PhoneNo { Prefix = "087", PhoneNumber = "2262997" }),
                new Contact(9, "Mary", "Roslyn", new PhoneNo { Prefix = "089", PhoneNumber = "6119774" }),
                new Contact(10, "Julia", "Barrington", new PhoneNo { Prefix = "085", PhoneNumber = "7291186" })
            };

        }

        public bool FindElementById(Func<Contact, bool> findFunc)
        {
            if(contacts.Count(findFunc) > 0)
            {
                return true;
            }

            return false;
        }

        public IEnumerable<Contact> GetAllElements()
        {
            return contacts;
        }

        public IEnumerable<Contact> GetAnyElements(Func<Contact, bool> func)
        {
            return contacts.Where(func);
        }
    }
}