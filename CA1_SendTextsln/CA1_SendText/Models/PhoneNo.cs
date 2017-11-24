using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace CA1_SendText.Models
{
    public class PhoneNo
    {
        
        public string Prefix { get; set; }
        
        public string PhoneNumber { get; set; }

       
    }
}