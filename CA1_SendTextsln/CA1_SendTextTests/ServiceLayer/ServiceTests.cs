using Microsoft.VisualStudio.TestTools.UnitTesting;
using CA1_SendText.ServiceLayer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CA1_SendText.Models;

namespace CA1_SendText.ServiceLayer.Tests
{
    [TestClass()]
    public class ServiceTests
    {
        Service service = new Service(new MockDatabase());

        [TestMethod()]
        public void GetContactsTestIfSizeIsEqual()
        {

            Assert.AreEqual(service.GetContacts().Count(), 10);

        }


        [TestMethod()]
        public void GetContactByPhoneNumberTestGetCorrectContact()
        {

            Contact contact = new Contact(9, "Mary", "Roslyn", new PhoneNo { Prefix = "089", PhoneNumber = "6119774" });
            Assert.ReferenceEquals(service.GetContactByPhoneNumber(new PhoneNo { Prefix = "089", PhoneNumber = "6119774" }), contact);

        }

        [TestMethod()]
        public void GetContactByPhoneNumberTestIfWrongNumber()
        {

            Contact contact = new Contact(9, "Mary", "Roslyn", new PhoneNo { Prefix = "089", PhoneNumber = "6119774" });
            Assert.AreNotEqual(service.GetContactByPhoneNumber(new PhoneNo { Prefix = "088", PhoneNumber = "6119774" }), contact);

        }

        [TestMethod()]
        public void GetContactByIdTestGetCorrectContact()
        {

            Contact contact = new Contact(9, "Mary", "Roslyn", new PhoneNo { Prefix = "089", PhoneNumber = "6119774" });
            Assert.ReferenceEquals(service.GetContactById(9), contact);

        }

        [TestMethod()]
        public void GetContactByIdTestIfIdWrong()
        {

            Contact contact = new Contact(9, "Mary", "Roslyn", new PhoneNo { Prefix = "089", PhoneNumber = "6119774" });
            Assert.AreNotEqual(service.GetContactById(7), contact);

        }
    }
}