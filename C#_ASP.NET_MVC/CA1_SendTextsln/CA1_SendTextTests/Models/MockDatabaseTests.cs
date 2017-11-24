using Microsoft.VisualStudio.TestTools.UnitTesting;
using CA1_SendText.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CA1_SendText.Models.Tests
{
    [TestClass()]
    public class MockDatabaseTests
    {
        MockDatabase db = new MockDatabase();

        [TestMethod()]
        public void GetAnyElementsTestIfGetCorrectContact()
        {

            Contact contact = new Contact(3, "Robert", "Mallow", new PhoneNo { Prefix = "086", PhoneNumber = "9987023" });
            Assert.ReferenceEquals(db.GetAnyElements(x => x.Id == 3).First(), contact);

        }

        [TestMethod()]
        public void GetAnyElementsTestIfIdNotExists()
        {

            IEnumerable<Contact> c = db.GetAnyElements(x => x.Id == 11);
            Assert.IsTrue(c.Count() == 0);

        }

        [TestMethod()]
        public void FindElementByIdTest()
        {
            Assert.IsTrue(db.FindElementById(x => x.Id == 2));
        }

        [TestMethod()]
        public void FindElementByIdTestIfIdNotExist()
        {
            Assert.IsFalse(db.FindElementById(x => x.Id == 11));
        }
    }
}