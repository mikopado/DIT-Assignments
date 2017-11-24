using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace CA1_SendText.Models
{
    public class ConfirmPageViewModel
    {
        [Display(Name = "Recipient")]
        public string NameRecipient { get; set; }

        [Display(Name = "Text Message")]
        public string TextMessage { get; set; }
    }
}