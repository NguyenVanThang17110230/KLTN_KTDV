import React from "react";
// import { Modal } from 'reactstrap'

const AddNewDocumentModal = ({closeModal}) => {
  console.log("okiii");
  return (
    <div className="fixed z-10 top-0 left-0 flex justify-center w-full h-full items-center bg-black bg-opacity-50 antialiased">
      <div className="border w-3/6 border-gray-300 shadow-xl box-border">
        <div className="flex flex-row justify-between p-6 bg-white border-b border-gray-200 rounded-tl-lg rounded-tr-lg">
          <p className="font-semibold text-gray-800">Add a step</p>
          <svg
            className="w-6 h-6 cursor-pointer"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
            onClick={() =>closeModal()}
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </div>
      </div>
    </div>
  );
};

export default AddNewDocumentModal;
