using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace CA1_SendText.Models
{
    public class SMS
    {
        
        public string TextMessage { get; set; }

        [Display(Name = "Phone Number")]
        public PhoneNo PhoneNumber { get; set; }

       
    }
}