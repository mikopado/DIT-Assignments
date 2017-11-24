using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace CA1_SendText.Models
{
    public class Contact
    {
        public int Id { get; set; }

        [Display(Name = "First Name" )]
        public string FirstName { get; set; }

        [Display(Name = "Last Name")]
        public string LastName { get; set; }

        [Display(Name = "Phone Number")]
        public PhoneNo PhoneNumber { get; set; }

        public Contact(int id, string fname, string lname, PhoneNo phoneNo)
        {
            Id = id;
            FirstName = fname;
            LastName = lname;
            PhoneNumber = phoneNo;
        }
    }
}