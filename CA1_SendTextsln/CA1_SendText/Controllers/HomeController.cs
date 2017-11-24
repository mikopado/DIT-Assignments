using CA1_SendText.Models;
using CA1_SendText.ServiceLayer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CA1_SendText.Controllers
{
    public class HomeController : Controller
    {
        Service service = new Service(new MockDatabase());

        public ActionResult Index(int id=0)
        {  
            //if the id is equal to zero or element not exists return the normal Index view with empty text box
            if(!service.Find(id))
            {
                return View();
            }

            //if this page is reached from the contact list an id will be passed. This id needs to retrieve the contact phone number and fill the text boxs in
            SmsViewModel svm = new SmsViewModel()
            {
                PhoneNo = service.GetContactById(id).PhoneNumber.PhoneNumber,
                Prefix = service.GetContactById(id).PhoneNumber.Prefix
            };

            return View(svm);
        }

        [HttpPost]
        public ActionResult Index(SmsViewModel sms)
        {
            if (ModelState.IsValid)
            {
                
                return RedirectToAction("SmsConfirmation", sms);
            }
            return View();
        }

        //SMS confiramtion action. First it gets the contact from the model, if the contact is not null(so contact exists),
        // then display the confirm page view, otherwise launch the error page.
        public ActionResult SmsConfirmation(SmsViewModel sms)
        {
            
            Contact contact = service.GetContactByPhoneNumber(new PhoneNo { Prefix = sms.Prefix, PhoneNumber = sms.PhoneNo });

            if (contact != null)
            {
                //for log purposes. Get the contact and pass the contact name and text message to log class to display to Debug output                
                Log.Message(contact.FirstName + " " + contact.LastName, sms.TextMessage);

                ConfirmPageViewModel confirmPageVM = service.CreateConfirmPageViewModel(sms, contact);

                return View(confirmPageVM);
            }

            return RedirectToAction("Error");
            
        }

        public ActionResult Error()
        {
            return View();
        }
       
        
    }
}