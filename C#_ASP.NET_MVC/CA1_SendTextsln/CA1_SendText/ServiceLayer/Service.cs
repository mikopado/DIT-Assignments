using CA1_SendText.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CA1_SendText.ServiceLayer
{
    public class Service
    {
        public IDataAccess<Contact> Repository { get; set; }
       
        public Service(IDataAccess<Contact> rep)
        {
            Repository = rep;
        }

        public IEnumerable<Contact> GetContacts()
        {
            return Repository.GetAllElements();
        }

        public Contact GetContactByPhoneNumber(PhoneNo phoneNo)
        {
            return Repository.GetAnyElements(x => x.PhoneNumber.Prefix.Equals(phoneNo.Prefix) && 
                    x.PhoneNumber.PhoneNumber.Equals(phoneNo.PhoneNumber)).FirstOrDefault();
        }

        public ConfirmPageViewModel CreateConfirmPageViewModel(SmsViewModel sms, Contact cont)
        {
            return new ConfirmPageViewModel
            {
                NameRecipient = cont.FirstName + " " + cont.LastName,
                TextMessage = sms.TextMessage
            };
        }

        public Contact GetContactById(int id)
        {
            
             return Repository.GetAnyElements(x => x.Id == id).FirstOrDefault();           
            
        }

        public bool Find(int id)
        {
            return Repository.FindElementById(x => x.Id == id);
        }
    }
}