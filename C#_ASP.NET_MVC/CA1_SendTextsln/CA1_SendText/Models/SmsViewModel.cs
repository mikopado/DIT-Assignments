using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace CA1_SendText.Models
{
    public class SmsViewModel
    {
        [Required]
        [Display(Name = "Text Message")]
        [StringLength(maximumLength: 140, ErrorMessage = "Message cannot be longer than 140 charachters.")]
        public string TextMessage { get; set; }


        [Required(ErrorMessage = "Prefix is required.")]
        [Display(Name = "Phone Number")]        
        [StringLength(maximumLength: 3, MinimumLength = 3, ErrorMessage = "Prefix must be 3 digits long.")]
        [RegularExpression("([0-9]*)", ErrorMessage = "Prefix must be a number.")]
        public string Prefix { get; set; }


        [Required(ErrorMessage = "Phone Number is required.")]
        [StringLength(maximumLength: 7, MinimumLength = 7, ErrorMessage = "Phone Number must be 7 digits long.")]
        [RegularExpression("([0-9]*)", ErrorMessage = "Phone number must be a number.")]
        public string PhoneNo { get; set; }
    }
}