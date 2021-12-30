import React from "react";
import Image from "next/image";
import moment from "moment";
import toastr from "toastr";
import { accountService } from "../../RestConnector";

const ViewProfileUserModal = ({ style, value, closeModal }) => {
  console.log("data-edit-ne", value);

  const lockAccount = async () => {
    const { id } = value;
    if (id) {
      try {
        const dataAPI = await accountService.lockAccount(id);
        console.log("sss", dataAPI);
        toastr.success("Lock account success")
      } catch (e) {
        let msg;
        switch (e.code) {
          default: {
            msg = e.message;
          }
        }
        toastr.error(msg);
      }
    }
  };
  return (
    <div
      className={
        "fixed p-0 top-0 left-0 right-0 bottom-0 flex justify-center w-full h-full  bg-black bg-opacity-50 antialiased overflow-x-hidden overflow-y-auto " +
        (style ? "opacity-1 visible z-10" : "opacity-0 invisible z-0")
      }
      style={{ transition: "all 0.4s" }}
    >
      <div className="my-10 mx-auto w-auto relative lg:max-w-xl lg:min-w-1/2">
        <div className="border-gray-300 shadow-xl box-border w-full bg-white p-6">
          <div className="flex justify-between items-center mb-5">
            <div className="text-2xl font-semibold text-gray-700">
              Information User
            </div>
            <div
              className="text-xl font-semibold text-gray-700 cursor-pointer"
              onClick={closeModal}
            >
              x
            </div>
          </div>

          <div className="flex items-center">
            <Image
              alt="..."
              className="w-full align-middle rounded-full"
              src={"/static/img/avt.jpg"}
              width={150}
              height={150}
            />
            <div className="ml-5">
              <div className="mt-3 font-bold text-gray-600 text-lg">
                {value.firstName && value.firstName.concat(" ", value.lastName)}
              </div>
              <div className="">{value.email}</div>
            </div>
          </div>
          <div className="grid grid-cols-2 gap-2 box-border mt-3">
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Id
              </label>
              <input
                value={value.id}
                className="appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm"
                type="text"
                disabled
              />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Gender
              </label>
              <input
                value={value.gender}
                className="appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm"
                type="text"
                disabled
              />
            </div>
          </div>
          <div className="grid grid-cols-2 gap-2 box-border mt-3">
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Date of birth
              </label>
              <input
                value={moment(value.dob).format("YYYY-MM-DD")}
                className="appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm"
                type="text"
                disabled
              />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">
                User code
              </label>
              <input
                value={value.userCode}
                className="appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm"
                type="text"
                disabled
              />
            </div>
          </div>
          <div className="grid grid-cols-2 gap-2 box-border mt-3">
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Status
              </label>
              <input
                value={value.isActive ? "Active" : "In Active"}
                className="appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm"
                type="text"
                disabled
              />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Phone number
              </label>
              <input
                value={value.phoneNumber}
                className="appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm"
                type="text"
                disabled
              />
            </div>
            <div className="flex">
              <button
                className="flex items-center justify-center bg-gradient-to-r from-green-400 to-blue-500 hover:from-pink-500 hover:to-yellow-500 text-white py-2 px-10 rounded focus:outline-none focus:shadow-outline font-semibold disabled:cursor-not-allowed mr-2"
                onClick={() => lockAccount()}
              >
                Lock account
              </button>
              <button
                className="flex items-center justify-center bg-gradient-to-r from-green-400 to-blue-500 hover:from-pink-500 hover:to-yellow-500 text-white py-2 px-10 rounded focus:outline-none focus:shadow-outline font-semibold disabled:cursor-not-allowed"
                onClick={closeModal}
              >
                Close
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewProfileUserModal;
