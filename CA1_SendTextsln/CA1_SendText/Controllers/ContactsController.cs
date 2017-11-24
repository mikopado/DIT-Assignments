using CA1_SendText.Models;
using CA1_SendText.ServiceLayer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CA1_SendText.Controllers
{
    public class ContactsController : Controller
    {
        // GET: Contacts
        public ActionResult Index()
        {
            Service service = new Service(new MockDatabase());
            
            return View(service.GetContacts());
        }

    }
}